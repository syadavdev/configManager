let editors = {};  // Object to hold references to all the Monaco editor instances

function showToast(message) {
    const toastElement = document.getElementById('notificationToast');
    toastElement.querySelector('.toast-body').textContent = message;
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}

window.onload = function() {
    require.config({ paths: { 'vs': 'https://cdnjs.cloudflare.com/ajax/libs/monaco-editor/0.44.0/min/vs' }});

    require(['vs/editor/editor.main'], function() {

        document.querySelectorAll('.tab-pane').forEach(pane => {
            const editorId = 'editor-' + pane.id;
            const editorElement = document.getElementById(editorId);
            // Find the child div element with the class editor-container
            const editorContainer = pane.querySelector('.editor-container');

            // Get the data-language attribute from the editorContainer element
            let language = editorContainer.getAttribute('data-language');

            // Make GET request to the API endpoint to fetch the content
            fetch(`http://localhost:18080/api/${pane.id}/getconfiguration`)
                .then(response => response.text())
                .then(content => {
                    editors[editorId] = monaco.editor.create(editorElement, {
                        value: content,
                        language: language.toLowerCase(),  // Use the language here
                        scrollbar: {
                            vertical: 'auto',
                            horizontal: 'auto'
                        },
                        theme: 'vs-dark',
                        automaticLayout: true,
                    });
                })
                .catch(error => {
                    console.error(`Error fetching content for ${editorId}: ${error}`);
                });
        });
    });

    // Listen for the Bootstrap tab shown event to refresh the Monaco editors
    document.getElementById('fileTabs').addEventListener('shown.bs.tab', function(e) {
        const targetId = e.target.getAttribute('href').substring(1);  // Get the id of the activated tab pane
        const editorId = 'editor-' + targetId;
        if (editors[editorId]) {
            setTimeout(function() {  // Use a timeout to delay the refresh
                editors[editorId].layout();  // Refresh the Monaco editor
            }, 300);  // Delay time in milliseconds (adjust as needed)
        }
    });

    const editorContainers = document.querySelectorAll('.editor-container');
    editorContainers.forEach(container => {
        container.style.height = (window.innerHeight - 200) + 'px';
    });
}
