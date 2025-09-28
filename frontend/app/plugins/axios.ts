import axios from 'axios';
import { defineNuxtPlugin } from 'nuxt/app';

export default defineNuxtPlugin(() => {
    const api = axios.create({
        baseURL: 'http://localhost:8080',
        withCredentials: true, // Cookie送受信を有効化
    });

    // アクセストークンの保存
    const accessToken = useState<string | null>('accessToken', () => null);

    const router = useRouter();

    //リクエストにアクセストークンの付与
    api.interceptors.request.use((config) => {
        if (accessToken.value) {
            config.headers.Authorization = `Bearer ${accessToken.value}`;
        }
        return config;
    });

    // レスポンス401のときはリフレッシュ
    api.interceptors.response.use(
        (res) => res,
        async (error) => {
            if (error.response?.status === 401) {
                try {
                    const res = await api.post('/api/refresh')
                    accessToken.value = res.data.accessToken
                    error.config.headers.Authorization = `Bearer ${accessToken.value}`
                    return api.request(error.config)
                } catch (e) {
                    console.log(e);
                    await logout() // 共通処理を使う
                    return Promise.reject(e)
                }
            }
            return Promise.reject(error)
        }
    )

    const signup = async (displayname: string, email: string, password: string) => {
        const res = await api.post('/api/signup', { displayname: displayname, email: email, password: password });
        accessToken.value = res.data.accessToken;
    }

    const login = async (email: string, password: string) => {
        try {
            const res = await api.post('/api/login', { email: email, password: password });
            accessToken.value = res.data.accessToken;
            console.log("accessToken" + accessToken.value);
            router.push("/mypage");
        } catch (e) {
            console.log(e);
            alert("ログイン失敗");
        }
    }

    const { setMessage } = useFlashMessage()

    const logout = async () => {
        try {
            //サーバ側でもセッション破棄
            await api.post(`/api/logout`);

        } catch (e) {
            console.warn("ログアウト失敗", e);

        } finally {
            //クライアント側でも破棄
            accessToken.value = null;
            setMessage("I logged out") // ← ここで設定
            router.push("/");
        }
    }

    return {
        provide: { api, accessToken, signup, login, logout },
    };

});