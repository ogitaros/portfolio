<script setup>
useHead({
    meta: [{ name: "robots", content: "noindex, nofollow" }],
});
const { user } = useUser();
</script>

<template>
    <main class="flex-grow bg-gray-100">
        <div class="py-20 px-6">
            <!-- プロフィールカード -->
            <div v-if="user" class="bg-white rounded-lg shadow p-6 flex items-center gap-6 mb-10">
                <div>
                    <h2 class="text-2xl font-bold text-gray-800">
                        {{ user.displayname }}
                        <span class="text-base text-gray-600"> logged in! Thank you for reading this far despite your busy schedule!!! </span>
                    </h2>
                    <p class="text-gray-600">{{ user.email }}</p>
                </div>
            </div>

            <!-- ダッシュボード風カード -->
            <div class="grid gap-6 md:grid-cols-2">
                <div class="bg-white rounded-lg shadow p-6">
                    <h3 class="font-semibold mb-2">Notice</h3>
                    <p class="text-gray-600 text-sm">There is currently no system maintenance scheduled.</p>
                </div>
            </div>
        </div>
    </main>
</template>
