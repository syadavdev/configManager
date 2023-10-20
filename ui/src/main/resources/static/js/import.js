// Updated script
// to handle button click instead of input change

// New function to handle file upload
function handleFileUpload() {
    const fileInput = document.getElementById('fileInput');
    const file = fileInput.files[0];
    if (file) {
        const formData = new FormData();
        formData.append('file', file);

        fetch('http://localhost:18080/api/import', {
            method: 'POST',
            body: formData,
        })
            .then(response => response.json())
            .then(data => {
                showToast(data);
            })
            .catch(error => {
                showToast('Error: ' + error.message);
            });
    } else {
        showToast('No file selected.');
    }
}

// Event listener for the upload button
document.getElementById('uploadButton').addEventListener('click', handleFileUpload);
