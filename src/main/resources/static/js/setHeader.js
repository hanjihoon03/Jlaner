async function fetchWithAuth(url, options = {}) {
    const token = getAccessToken();
    console.log(token);

    if (!options.headers) {
        options.headers = {};
    }

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, options);
    return response;
}