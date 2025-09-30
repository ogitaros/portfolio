export default defineNuxtRouteMiddleware(async (to, from) => {
    //スキップページ
    if (['/', '/chuta'].includes(to.path)) return

    const accessToken = useState<string | null>('accessToken')
    const { $refresh } = useNuxtApp()

    if (!accessToken.value) {
        try {
            const newToken = await $refresh()
            accessToken.value = newToken

        } catch (e) {

        }
    }

    if (accessToken.value && (to.path === '/login' || to.path === '/signup')) {
        return navigateTo('/mypage')
    }
})
