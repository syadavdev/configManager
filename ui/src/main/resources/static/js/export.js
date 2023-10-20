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
    // ...
}
