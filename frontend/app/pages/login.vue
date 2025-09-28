<script setup lang="ts">
const email = ref("");
const password = ref("");
const { $login } = useNuxtApp();

const doLogin = async () => {
    await $login(email.value, password.value);
};
</script>

<template>
    <div class="bg-gray-200 flex-1 justify-center">
        <!-- ログインフォーム -->
        <form @submit.prevent="doLogin" class="mx-auto max-w-md rounded-lg border shadow bg-white justify-center mt-20">
            <div class="flex flex-col gap-6 p-6">
                <!-- Email -->
                <div>
                    <label for="email" class="mb-2 block text-sm font-medium text-gray-600"> Email </label>
                    <input v-model="email" id="email" name="email" type="email" placeholder="you@example.com" class="w-full rounded border bg-gray-50 px-3 py-2 text-gray-800 outline-none focus:ring-2 focus:ring-gray-400 transition" />
                </div>

                <!-- Password -->
                <div>
                    <label for="password" class="mb-2 block text-sm font-medium text-gray-600">Password</label>
                    <input v-model="password" id="password" name="password" type="password" placeholder="••••••••" class="w-full rounded border bg-gray-50 px-3 py-2 text-gray-800 outline-none focus:ring-2 focus:ring-gray-400 transition" />
                </div>

                <!-- ログインボタン -->
                <button type="submit" class="block w-full rounded-lg bg-gray-700 px-6 py-3 text-white font-semibold transition hover:bg-gray-600 active:bg-gray-800">Log in</button>

                <!-- 区切り線
                <div class='relative flex items-center'>
                  <span class='flex-grow border-t border-gray-300'></span>
                  <span class='px-3 text-sm text-gray-400 bg-white'>or</span>
                  <span class='flex-grow border-t border-gray-300'></span>
                </div>
                -->

                <!-- Googleログイン
                <button
                  type='button'
                  class='flex items-center justify-center gap-2 rounded-lg border border-gray-300 bg-white px-6 py-3 font-medium text-gray-800 transition hover:bg-gray-100 active:bg-gray-200'>
                  <img src='/logos/google-icon.svg' alt='Google' class='h-5 w-5' />
                  Continue with Google
                </button>
                -->

                <!-- GitHubログイン
                <button type='button' class='flex items-center justify-center gap-2 rounded-lg bg-gray-800 px-6 py-3 font-medium text-white transition hover:bg-gray-700 active:bg-gray-900'>
                  <img src='/logos/github-mark-white.svg' alt='GitHub' class='h-5 w-5' />
                  Continue with GitHub
                </button>
                -->
            </div>

            <div class="flex items-center justify-center bg-gray-100 p-4">
                <p class="text-sm text-gray-600">
                    アカウントをお持ちでないですか？
                    <NuxtLink to="/signup" class="text-indigo-500 hover:text-indigo-600 font-medium"> Register </NuxtLink>
                </p>
            </div>
        </form>
    </div>
</template>
