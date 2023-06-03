document.onreadystatechange = () => {
    if (document.readyState === 'complete') {
        // Set the back button to go to the previous page
        const referer = document.referrer;
        console.log(referer);
        if (referer !== '') {
            const footerBackLink = document.getElementById('footer-back-link');
            footerBackLink.href = referer;
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