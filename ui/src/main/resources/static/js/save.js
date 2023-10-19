function save() {
    console.log('Save button clicked');
    const activePaneId = document.querySelector('.tab-pane.show.active').id;
    const editorId = 'editor-' + activePaneId;
    const editorContent = editors[editorId].getValue();  // Get the current content of the active editor
    const activePaneName = document.querySelector(`#tab-${activePaneId}`).textContent;

    const formData = new FormData();
    const file = new Blob([editorContent], { type: 'text/plain' });

    formData.append('file', file);

    fetch(`http://localhost:18080/api/${activePaneId}/saveconfiguration`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .then(data => {
            console.log('File saved successfully:', data);
            showToast('File saved successfully');
            showToast(`"${activePaneName}" Configuration saved successfully.`);
        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}
