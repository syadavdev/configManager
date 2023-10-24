let editors = {};  // Object to hold references to all the Monaco editor instances
let unsavedChanges = {};  // Object to track unsaved changes


function exportData() {
    console.log('Export button clicked');

    // Iterate through the unsavedChanges object to find which tabs have unsaved changes
    for (const editorId in unsavedChanges) {
        if (unsavedChanges.hasOwnProperty(editorId) && unsavedChanges[editorId]) {
            // Call the save function for each tab with unsaved changes
            console.log(`Saving ${editorId}...`)
            save(editorId);
        }
    }

    // Now proceed with your export logic
    fetch('http://localhost:18080/convert-to-bundle', {
        method: 'GET',
        headers: {
            'Accept': 'application/zip',
        },
    })
        .then(response => {
            if (!response.ok) {
                const errorText = 'Error in exporting Configurations ' + response.json();
                showToast(errorText);  // Display the error message using a toast
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.blob();  // Convert the response to a Blob object
        })
        .then(blob => {
            const url = window.URL.createObjectURL(blob);
            const a = document.createElement('a');
            a.style.display = 'none';
            a.href = url;
            a.download = 'bundle.zip';  // Set the desired file name here

            document.body.appendChild(a);
            a.click();

            window.URL.revokeObjectURL(url);

            // Display a success toast
            showToast('Zip file downloaded successfully.');
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}


function getActiveEditorId() {
    const activePaneId = document.querySelector('.tab-pane.show.active').id;
    return 'editor-' + activePaneId;
}


// Function to update the enabled/disabled state of the "Save" button
function updateSaveButtonState() {
    const activePaneId = document.querySelector('.tab-pane.show.active').id;
    const editorId = 'editor-' + activePaneId;
    const saveButton = document.querySelector('.btn.btn-primary');
    saveButton.disabled = !unsavedChanges[editorId];  // Disable the button if there are no unsaved changes
}

function initialSetup() {
    require.config({paths: {'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.44.0/min/vs'}});

    require(['vs/editor/editor.main'], function () {
        setupEditors();
    });
}

function setupEditors() {
    document.querySelectorAll('.tab-pane').forEach(pane => {
        const editorId = 'editor-' + pane.id;
        const editorElement = document.getElementById(editorId);
        const editorContainer = pane.querySelector('.editor-container');
        const language = editorContainer.getAttribute('data-language');

        fetch(`http://localhost:18080/api/${pane.id}/getconfiguration`)
            .then(response => response.text())
            .then(content => {
                createEditor(editorElement, language, content, editorId);
            })
            .catch(error => {
                console.error(`Error fetching content for ${editorId}: ${error}`);
            });
    });
}

function createEditor(editorElement, language, content, editorId) {
    const editor = monaco.editor.create(editorElement, {
        value: content,
        language: language.toLowerCase(),
        scrollbar: {vertical: 'auto', horizontal: 'auto'},
        theme: 'vs-dark',
        automaticLayout: true,
    });
    editors[editorId] = editor;
    unsavedChanges[editorId] = false;

    editor.onDidChangeModelContent(event => {
        unsavedChanges[editorId] = true;
        updateSaveButtonState();
    });
}

function refreshEditors(e) {
    const targetId = e.target.getAttribute('href').substring(1);
    const editorId = 'editor-' + targetId;
    if (editors[editorId]) {
        setTimeout(() => {
            editors[editorId].layout();
        }, 300);
        setTimeout(updateSaveButtonState, 300);
    }
}

function adjustEditorContainerHeight() {
    const editorContainers = document.querySelectorAll('.editor-container');
    editorContainers.forEach(container => {
        container.style.height = (window.innerHeight - 200) + 'px';
    });
}

window.onload = function () {
    initialSetup();
    document.getElementById('fileTabs').addEventListener('shown.bs.tab', refreshEditors);
    adjustEditorContainerHeight();
}
