function showToast(message) {
    const toastElement = document.getElementById('notificationToast');
    toastElement.querySelector('.toast-body').textContent = message;
    const toast = new bootstrap.Toast(toastElement);
    toast.show();
}

