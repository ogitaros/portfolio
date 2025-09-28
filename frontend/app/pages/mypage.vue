<script setup lang="ts">
definePageMeta({
    middleware: ["auth"],
});

const user = useAuthUser();
</script>

<template>
    <main class="flex-grow bg-gray-100">
        <div class="py-20 px-6">
            <!-- プロフィールカード -->
            <div class="bg-white rounded-lg shadow p-6 flex items-center gap-6 mb-10">
                <img :src="user.avatar" alt="avatar" class="w-24 h-24 rounded-full object-cover" />
                <div>
                    <h2 class="text-2xl font-bold text-gray-800">{{ user?.displayname }}<span class="text-base text-gray-600">さん、ログインありがとうございます</span></h2>
                    <p class="text-gray-600">{{ user?.email }}</p>
                </div>
            </div>

            <!-- ダッシュボード風カード -->
            <div class="grid gap-6 md:grid-cols-2">
                <div class="bg-white rounded-lg shadow p-6">
                    <h3 class="font-semibold mb-2">最近のアクティビティ</h3>
                    <ul class="text-gray-600 text-sm space-y-1">
                        <li>🔹 プロフィールを更新しました</li>
                        <li>🔹 新しい記事を投稿しました</li>
                        <li>🔹 パスワードを変更しました</li>
                    </ul>
                </div>

                <div class="bg-white rounded-lg shadow p-6">
                    <h3 class="font-semibold mb-2">お知らせ</h3>
                    <p class="text-gray-600 text-sm">現在システムメンテナンスは予定されていません。</p>
                </div>
            </div>
        </div>
    </main>
</template>
