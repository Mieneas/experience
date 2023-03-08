const serverConfig = {
    api: {
        baseUrl: 'http://localhost:8081'
    },
    routerOptions: {
        get: '?username=',
        post: '/data',
        main: '/dashboard'
    }
}

export default serverConfig