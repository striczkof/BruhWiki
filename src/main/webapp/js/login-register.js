document.onreadystatechange = () => {
    if (document.readyState === 'complete') {
        // Adds confirm password to the form
        const registerForm = document.getElementById('register-form');
        const changePassForm = document.getElementById('change-pass-form');
        const deleteAccountForm = document.getElementById('delete-user-form');
        if (registerForm || changePassForm || deleteAccountForm) {
            const confirmPasswordLabelLI = document.createElement('li');
            confirmPasswordLabelLI.className = 'form-list-item';
            const confirmPasswordLabel = document.createElement('label');
            confirmPasswordLabel.htmlFor = 'confirm-password';
            confirmPasswordLabel.textContent = 'Confirm Password (required)';
            confirmPasswordLabelLI.appendChild(confirmPasswordLabel);
            const confirmPasswordInputLI = document.createElement('li');
            confirmPasswordInputLI.className = 'form-list-item';
            let confirmPasswordInput = document.createElement('input');
            // <input type="password" name="password" id="password" placeholder="Password" required/>
            confirmPasswordInput.type = 'password';
            confirmPasswordInput.name = 'confirmPassword';
            confirmPasswordInput.id = 'confirm-password';
            confirmPasswordInput.placeholder = 'Confirm Password';
            confirmPasswordInput.required = true;
            confirmPasswordInputLI.appendChild(confirmPasswordInput);
            const passwordInputLI = document.getElementsByClassName('form-list-input--password');
            for (let i = 0; i < passwordInputLI.length; i++) {
                passwordInputLI.item(i).insertAdjacentElement("afterend", confirmPasswordInputLI)
                passwordInputLI.item(i).insertAdjacentElement("afterend", confirmPasswordLabelLI)
            }
            // Validating deletion password if exists
            const passwordDelConf = document.getElementById('password-del');
            if (passwordDelConf) {
                const validatePasswordDelConf = () => {
                    if (passwordDelConf.value !== confirmPasswordInput.value) {
                        confirmPasswordInput.setCustomValidity('Passwords do not match');
                    } else {
                        confirmPasswordInput.setCustomValidity('');
                    }
                }
                passwordDelConf.onkeyup = validatePasswordDelConf;
                confirmPasswordInput.onkeyup = validatePasswordDelConf;
            } else {
                // Now validate the password and confirm password
                const passwordInput = document.getElementById('password');
                confirmPasswordInput = document.getElementById('confirm-password');
                const validatePassword = () => {
                    if (passwordInput.value !== confirmPasswordInput.value) {
                        confirmPasswordInput.setCustomValidity('Passwords do not match');
                    } else {
                        confirmPasswordInput.setCustomValidity('');
                    }
                }
                passwordInput.onchange = validatePassword;
                confirmPasswordInput.onkeyup = validatePassword;
            }
        }

        // Check if at least one field is filled in
        const changeNameForm = document.getElementById('change-name-form');
        if (changeNameForm) {
            const usernameInput = document.getElementById('username');
            const nameInput = document.getElementById('name');

            const checkIfNotBothEmpty = () => {
                if (usernameInput.value === '' && nameInput.value === '') {
                    nameInput.setCustomValidity('At least one field must be filled in');
                } else {
                    nameInput.setCustomValidity('');
                }
            }

            usernameInput.onkeyup = checkIfNotBothEmpty;
            nameInput.onkeyup = checkIfNotBothEmpty;
        }

        // Set the back button to go to the previous page
        const referer = document.referrer;
        console.log(referer);
        if (referer !== '') {
            const footerBackLink = document.getElementsByClassName('footer-back-link');
            for (let i = 0; i < footerBackLink.length; i++) {
                footerBackLink.item(i).href = referer;
            }
        }

        // Redirect to index.jsp after success
        const urlParams = new URLSearchParams(window.location.search);
        const result = urlParams.get('result');
        if (result === 'success') {
            setTimeout(() => {
                window.location.href = 'index.jsp';
            }, 5000);
        }
    }
};