export const useUser = () => {
    const user = useState<{ email: string; displayname: string } | null>('user', () => null)
    const { $fetchUser } = useNuxtApp()

    onMounted(() => {
        if (!user.value) {
            $fetchUser()
        }
    })

    return { user }
}