<template>
    <div class="p-4">
        <h2>JWT 検証ページ</h2>

        <div class="mb-2">
            <input v-model="username" placeholder="Username" />
            <input v-model="password" placeholder="Password" type="password" />
            <button @click="handleLogin">Login</button>
            <button @click="handleLogout">Logout</button>
            <button @click="getRole">ロール確認</button>
        </div>

        <div class="mb-2">
            <button @click="handleProtected">Call Protected API</button>
        </div>

        <div>
            <p>Status: {{ status }}</p>
            <p>Protected Response: {{ protectedMessage }}</p>
        </div>

        <div>
            <p><strong>ユーザーの Role:</strong></p>
            <pre>{{ userRole }}</pre>
        </div>
    </div>
</template>

<script setup lang="ts">
const { $login, $logout, $callProtected, $getUserData, $getAdminData } = useNuxtApp();

const username = ref("syouta139@gmail.com");
const password = ref("syouta139");
const status = ref("Not logged in");
const protectedMessage = ref("");
const apiResponse = ref("");
const userRole = ref("");

const handleLogin = async () => {
    try {
        await $login(username.value, password.value);
        status.value = "Logged in!";
    } catch (e) {
        status.value = "Login failed";
        console.error(e);
    }
};

const handleLogout = async () => {
    await $logout();
    status.value = "Logged out";
};

const handleProtected = async () => {
    try {
        const msg = await $callProtected();
        protectedMessage.value = msg;
    } catch (e) {
        console.error(e);
        protectedMessage.value = "Unauthorized";
    }
};

// ロール確認API呼び出し
const getRole = async () => {
    try {
        const role = await $getUserData();
        if (role !== "USER") {
            const role = await $getAdminData();
        }

        userRole.value = res.data.role;
        apiResponse.value = "ロール確認成功";
    } catch (err: any) {
        apiResponse.value = "ロール確認失敗: " + err.message;
    }
};
</script>
