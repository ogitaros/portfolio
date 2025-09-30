import axios from 'axios';
import { defineNuxtPlugin } from 'nuxt/app';

export default defineNuxtPlugin(() => {

    // アクセストークンの保存
    const accessToken = useState<string | null>('accessToken', () => null);
    const router = useRouter();

    // エラーメッセージなど
    const { setMessage, clearMessage } = useFlashMessage()

    // 認証付きAPI（interceptorあり）
    const api = axios.create({
        baseURL: 'http://localhost:8080/api',
        withCredentials: true,
    })

    //リクエストにアクセストークンの付与
    api.interceptors.request.use((config) => {
        if (accessToken.value) {
            config.headers.Authorization = `Bearer ${accessToken.value}`;
        }
        return config;
    });

    // レスポンス401のときはリフレッシュ
    // api.interceptors.response.use(
    //     (res) => res,
    //     async (error) => {
    //         const original = error.config

    //         // refresh 自体が失敗したら強制ログアウト
    //         if (original?.url?.includes('/refresh')) {
    //             await logout()
    //             return Promise.reject(error)
    //         }

    //         if (error.response?.status === 401) {
    //             try {
    //                 const newToken = await refresh()
    //                 accessToken.value = newToken
    //                 original.headers.Authorization = `Bearer ${newToken}`
    //                 return api.request(original)
    //             } catch (e: any) {
    //                 // refresh が 401 or 403 の場合はログアウト
    //                 if ([401, 403].includes(e.response?.status)) {
    //                     await logout()
    //                     return Promise.reject(e)
    //                 }
    //                 return Promise.reject(e)
    //             }
    //         }

    //         return Promise.reject(error)
    //     }
    // )
    const signup = async (displayname: string, email: string, password: string) => {
        try {
            clearMessage()
            if (!displayname || !email || !password) {
                setMessage("DisplayName and Email and password are required.")
                return
            }
            const res = await api.post('/signup', { displayname, email, password })
            accessToken.value = res.data.accessToken
            return navigateTo('/login')
        } catch (e) {
            setMessage("Signup failed!! Please check your DisplayName and Email and Password!!!")
        }
    }

    const login = async (email: string, password: string) => {
        try {
            clearMessage()
            if (!email || !password) {
                setMessage("Email and password are required.")
                return
            }
            const res = await api.post('/login', { email, password })
            accessToken.value = res.data.accessToken
            console.log("accessToken" + accessToken.value);
            return navigateTo('/mypage')
        } catch (e) {
            setMessage("Login failed!! Please check your Email and Password!!!")
        }
    }

    const logout = async () => {
        try {
            //サーバ側でもセッション破棄
            await api.post(`/logout`);
        } catch (e) {
            console.warn("ログアウト失敗", e);
        } finally {
            //クライアント側でも破棄
            accessToken.value = null;
            setMessage("I logged out!!!thank you for letting me help you all the way through to the end🙏") // ← ここで設定
            navigateTo("/");
        }
    }

    const user = useState<{ email: string; displayname: string } | null>('user', () => null)

    const fetchUser = async () => {
        try {
            const res = await api.get(`/user`);
            accessToken.value = res.data.accessToken;
            user.value = res.data.user
        } catch (e) {
            console.error('ユーザー情報取得失敗', e)
            user.value = null
        }
    }

    // refresh用
    const refreshapi = axios.create({
        baseURL: 'http://localhost:8080/api',
        withCredentials: true,
    })

    const refresh = async (): Promise<string> => {
        console.log("refresh API 実行中")
        const res = await refreshapi.post(`/refresh`);
        if (res === null) {
            accessToken.value = null;
            return ''
        } else {
            const newAccessToken = res.data.accessToken
            accessToken.value = newAccessToken
            return newAccessToken
        }

    }

    return {
        provide: { api, accessToken, signup, login, logout, user, fetchUser, refreshapi, refresh },
    };

});