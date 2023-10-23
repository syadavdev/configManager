let apiList = [];
let newRowCounter = 1;
function handleCheckboxChange(event) {
    console.log('Checkbox change event triggered');
    let row = event.target.closest('.config-row');
    if (row) {
        let saveButton = row.querySelector('.save-button');
        if (saveButton) {
            let checkboxes = row.querySelectorAll('.api-checkbox');
            let isChanged = Array.from(checkboxes).some(checkbox => checkbox.checked !== (checkbox.dataset.originalState === 'true'));
            saveButton.disabled = !isChanged;
            if (isChanged) {
                // saveButton.addEventListener('click', handleSaveButtonClick);
                console.log('Save button enabled');
            } else {
                // saveButton.removeEventListener('click', handleSaveButtonClick);
                console.log('Save button disabled');
            }
        }
    }
}

async function fetchApiList() {
    try {
        const response = await fetch('/apis');
        if (!response.ok) {
            console.error('Failed to fetch API list:', response.statusText);
            return;
        }
        apiList = await response.json();
    } catch (error) {
        console.error('Error fetching API list:', error);
    }
}

async function addNewConfigRow() {
    let table = document.getElementById('config-table').getElementsByTagName('tbody')[0];
    let newRow = document.createElement('tr');
    newRow.id = 'new-row-' + newRowCounter;
    newRow.classList.add('config-row');

    // Configuration Name column
    let nameCell = document.createElement('td');
    let nameInput = document.createElement('input');
    nameInput.type = 'text';
    nameInput.placeholder = 'Enter Configuration Name';
    nameInput.className = 'form-control';
    nameCell.appendChild(nameInput);


    // API column
    let apiCell = document.createElement('td');

    // Fetch apiList if it's not already loaded
    if (apiList.length === 0) {
        await fetchApiList();
    }

    // Assume apiList is a global variable or fetch it accordingly
    apiList.forEach(api => {
        let div = document.createElement('div');
        div.className = 'form-check';

        let input = document.createElement('input');
        input.type = 'checkbox';
        input.className = 'form-check-input api-checkbox';
        input.id = 'new-' + api.id + '-' + newRowCounter;
        input.name = 'apiList';
        input.value = api.label;

        let label = document.createElement('label');
        label.className = 'form-check-label';
        label.htmlFor = input.id;
        label.textContent = api.label;

        div.appendChild(input);
        div.appendChild(label);
        apiCell.appendChild(div);
    });

    // Action column
    let actionCell = document.createElement('td');
    let saveButton = document.createElement('button');
    saveButton.type = 'button';
    saveButton.className = 'btn btn-primary save-button';
    saveButton.textContent = 'Save';
    saveButton.setAttribute('onclick', `handleSaveButtonClick('new-row-${newRowCounter}')`);
    actionCell.appendChild(saveButton);

    // Append cells to the new row
    newRow.appendChild(nameCell);
    newRow.appendChild(apiCell);
    newRow.appendChild(actionCell);

    // Append the new row to the table
    table.appendChild(newRow);

    newRowCounter++;
}

// document.addEventListener('DOMContentLoaded', (event) => {
//     document.getElementById('add-config-button').addEventListener('click', addNewConfigRow);
// });
//
// window.onload = (event) => {
//     console.log('DOM fully loaded and parsed');
//     document.querySelectorAll('.api-checkbox').forEach(checkbox => {
//         console.log('Storing original state for checkbox');
//         checkbox.dataset.originalState = checkbox.checked;
//         checkbox.addEventListener('change', handleCheckboxChange);
//     });
//     // document.getElementById('add-config-button').addEventListener('click', addNewConfigRow);
// };

document.addEventListener('DOMContentLoaded', async (event) => {
    await fetchApiList();  // Fetch apiList on page load
    document.getElementById('add-config-button').addEventListener('click', addNewConfigRow);
    document.querySelectorAll('.api-checkbox').forEach(checkbox => {
        console.log('Storing original state for checkbox');
        checkbox.dataset.originalState = checkbox.checked;
        checkbox.addEventListener('change', handleCheckboxChange);
    });
});

async function handleSaveButtonClick(configName) {

    // Adjust the rowId determination to handle both existing and new configurations
    let rowId = configName.startsWith('new-row-') ? configName : 'row-' + configName;
    console.log('Save button clicked for configName:', configName, 'rowId:', rowId)
    let row = document.getElementById(rowId);
    console.log(row)
    if (!row) {
        console.error('Row not found for configName:', configName);
        return;
    }

    let nameInput = row.querySelector('input[type="text"]');
    let name = nameInput ? nameInput.value : document.getElementById('hidden-' + configName).value;



    let checkboxes = row.querySelectorAll('.api-checkbox');
    let apiList = Array.from(checkboxes).filter(checkbox => checkbox.checked).map(checkbox => checkbox.value);

    // Construct a data object
    let data = {
        name: name,
        apiList: apiList
    };


    try {
        const response = await fetch('/configuration', {
            method: 'POST',
            body: JSON.stringify(data),
            headers: {
                'Content-Type': 'application/json',
                'Accept': 'application/json',
            },
        });

        if (!response.ok) {
            showToast('Network response was not ok: ' + response.statusText);
            return;
        }

        const responseData = await response.json();
        showToast(name + ":"+ responseData.message);  // Assuming the JSON object has a 'message' property
        // Disable the save button
        let saveButton = row.querySelector('.save-button');
        saveButton.disabled = true;

        // Add a 'saved' class to the row
        row.classList.add('saved');
        console.log(row)


    } catch (error) {
        console.error('There has been a problem with your fetch operation:', error);
        showToast('There has been a problem with your fetch operation: ' + error.message);
    }
}




