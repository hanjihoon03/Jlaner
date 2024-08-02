async function fetchWithAuth(url, options = {}) {
    const token = getAccessToken();

    if (!options.headers) {
        options.headers = {};
    }

    if (token) {
        options.headers['Authorization'] = `Bearer ${token}`;
    }

    const response = await fetch(url, options);
    return response;
}