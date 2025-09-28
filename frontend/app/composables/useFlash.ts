export const useFlashMessage = () => {
    const message = useState<string | null>("flash-message", () => null)

    const setMessage = (msg: string) => {
        message.value = msg
    }

    const clearMessage = () => {
        message.value = null
    }

    return { message, setMessage, clearMessage }
}
