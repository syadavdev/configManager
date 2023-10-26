function save(editorId) {
    console.log('Save button clicked for editor:', editorId);

    const editorContent = editors[editorId].getValue();  // Get the current content of the editor

    // Extract the paneId from the editorId
    const paneId = editorId.replace('editor-', '');
    const paneName = document.querySelector(`#tab-${paneId}`).textContent;

    const formData = new FormData();
    const file = new Blob([editorContent], {type: 'text/plain'});

    formData.append('file', file);

    fetch(`http://localhost:19090/api/${paneId}/saveconfiguration`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            unsavedChanges[editorId] = false;  // Mark this editor as saved
            updateSaveButtonState();
            return response.text();
        })
        .then(data => {
            console.log('File saved successfully:', data);
            showToast('File saved successfully');
            showToast(`"${paneName}" Configuration saved successfully.`);
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
