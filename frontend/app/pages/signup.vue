<script setup lang="ts">
middleware: ["guest-only"];
const displayname = ref("");
const email = ref("");
const password = ref("");
const { $signup } = useNuxtApp();

const doSignup = async () => {
    await $signup(displayname.value, email.value, password.value);
};

const { message } = useFlashMessage();
</script>

<template>
    <div class="bg-gray-200 flex-1 justify-center">
        <!-- Message -->
        <transition name="fade">
            <div v-if="message" class="pt-6 sm:pt-8 lg:pt-12">
                <div class="mx-auto max-w-screen-2xl px-4 pb-4 md:px-8">
                    <div class="relative flex flex-wrap rounded-lg bg-indigo-500 px-4 py-3 shadow-lg sm:flex-nowrap sm:items-center sm:justify-center sm:gap-3 sm:pr-8 md:px-8">
                        <div class="order-1 mb-2 inline-block w-11/12 max-w-screen-sm text-sm text-white sm:order-none sm:mb-0 sm:w-auto md:text-base font-bold">
                            {{ message }}
                        </div>
                    </div>
                </div>
            </div>
        </transition>
        <!-- ログインフォーム -->
        <form @submit.prevent="doSignup" class="mx-auto max-w-md rounded-lg border shadow bg-white justify-center mt-20">
            <div class="flex flex-col gap-6 p-6">
                <!-- ユーザ名 -->
                <div>
                    <label for="text" class="mb-2 block text-sm font-medium text-gray-600"> displayName </label>
                    <input v-model="displayname" id="displayname" name="Displayname" type="text" placeholder="ginotoro" class="w-full rounded border bg-gray-50 px-3 py-2 text-gray-800 outline-none focus:ring-2 focus:ring-gray-400 transition" />
                </div>

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
                <!-- 登録 -->
                <button type="submit" class="block w-full rounded-lg bg-gray-700 px-6 py-3 text-white font-semibold transition hover:bg-gray-600 active:bg-gray-800">signup</button>
            </div>
        </form>
    </div>
</template>
