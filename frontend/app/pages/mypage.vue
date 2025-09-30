<script setup>
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
                        <span class="text-base text-gray-600"> さん、ログイン中。お忙しい中、ここまでご参照ありがとうございます。 </span>
                    </h2>
                    <p class="text-gray-600">{{ user.email }}</p>
                </div>
            </div>

            <!-- ダッシュボード風カード -->
            <div class="grid gap-6 md:grid-cols-2">
                <div class="bg-white rounded-lg shadow p-6">
                    <h3 class="font-semibold mb-2">お知らせ</h3>
                    <p class="text-gray-600 text-sm">現在システムメンテナンスは予定されていません。</p>
                </div>
            </div>
        </div>
    </main>
</template>
