function reset() {
    console.log('Reset button clicked');
    const activePaneId = document.querySelector('.tab-pane.show.active').id;
    const editorId = 'editor-' + activePaneId;
    const activePaneName = document.querySelector(`#tab-${activePaneId}`).textContent;

    fetch(`http://localhost:19090/api/${activePaneId}/getconfiguration`)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok ' + response.statusText);
            }
            return response.text();
        })
        .then(content => {
            // Set the editor content to the fetched content
            editors[editorId].setValue(content);
            console.log('Editor content reset successfully');

            showToast(`"${activePaneName}" configuration content has been rollback to Previously Saved State.`);

        })
        .catch(error => {
            console.error('There has been a problem with your fetch operation:', error);
        });
}