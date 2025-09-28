import { defineNuxtRouteMiddleware, navigateTo, useNuxtApp } from '#app'

export default defineNuxtRouteMiddleware(async () => {
    const { $api } = useNuxtApp()
    const user = useAuthUser()
    const { setMessage } = useFlashMessage()

    if (!user.value) {
        try {
            const res = await $api.get("/api/user")
            //console.log(res)
        } catch (e) {
            setMessage("ログインしてください")
            console.log(e);
            return navigateTo("/login")
        }
    }
})