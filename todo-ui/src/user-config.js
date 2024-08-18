const serverConfig = {
    api: {
        baseUrl: 'https://server.grykely.de:8091'
    },
    routerOptions: {
        get: '?username=',
        post: '/data',
        main: '/dashboard'
    }
}

export default serverConfig