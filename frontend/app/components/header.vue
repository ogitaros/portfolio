<script setup>
const route = useRoute();
const { $accessToken, $logout } = useNuxtApp();
</script>

<template>
    <header class="flex items-center justify-between px-headerX py-headerY bg-brand-bg shadow text-white">
        <NuxtLink to="/" class="flex items-center gap-2 hover:text-brand-hover">
            <img src="/ginotoro_favicon.ico" alt="ginotoro Top" class="h-10 w-10" />
            <span class="text-lg font-bold">Ginotoro</span>
        </NuxtLink>

        <nav class="space-x-6">
            <a v-if="route.path === '/'" href="#about" class="hover:text-brand-hover">About</a>
            <a v-if="route.path === '/'" href="#skills" class="hover:text-brand-hover">Skills</a>
            <a v-if="route.path === '/'" href="#works" class="hover:text-brand-hover">Works</a>

            <!-- ログイン済み時 -->
            <button v-if="$accessToken" @click="$logout" class="hover:text-brand-hover">Logout</button>
        </nav>
    </header>
</template>
