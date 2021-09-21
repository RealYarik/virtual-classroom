const TOKEN_KEY = 'auth-token'

export const TokenStorage = {

    saveToken: (token: string) => {
        window.sessionStorage.removeItem(TOKEN_KEY)
        window.sessionStorage.setItem(TOKEN_KEY, token)
    },

    getToken: () => {
        return sessionStorage.getItem(TOKEN_KEY) as string;
    },

    logout: () => {
        window.sessionStorage.clear();
        window.location.reload();
    }
}