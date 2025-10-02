<script setup lang="ts">
import { ref, watch, onMounted } from "vue";

// 今日の年を初期値に
const year = ref(new Date().getFullYear());

// 祝日データを保持（例: { "2025-01-01": "元日", ... }）
const holidays = ref<Record<string, string>>({});

// 祝日データを取得する関数
const fetchHolidays = async (y: number) => {
    try {
        const res = await $fetch<Record<string, string>>(`https://holidays-jp.github.io/api/v1/${y}/date.json`);
        holidays.value = res || {};
    } catch (err) {
        console.error("祝日データ取得エラー:", err);
        holidays.value = {};
    }
};

// 初期読み込み
onMounted(async () => {
    await fetchHolidays(year.value); //
});

// 年が変わったら再取得
watch(year, (newYear) => {
    fetchHolidays(newYear);
});

// 12か月分の画像（サンプル）
const images = ["/chuta/jan.webp", "/chuta/feb.webp", "/chuta/mar.webp", "/chuta/apr.webp", "/chuta/may.webp", "/chuta/jun.webp", "/chuta/jul.webp", "/chuta/aug.webp", "/chuta/sep.webp", "/chuta/oct.webp", "/chuta/nov.webp", "/chuta/dec.webp"];

// 月ごとに日数を返す関数
const getDays = (month: number) => {
    return new Date(year.value, month, 0).getDate();
};

// 曜日ラベル
const week = ["日", "月", "火", "水", "木", "金", "土"];
</script>

<template>
    <main class="flex-grow bg-gray-100 py-10">
        <!-- ヘッダー -->
        <div class="flex items-center justify-center gap-6 my-3">
            <button @click="year--" class="p-2 bg-white rounded shadow">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                </svg>
            </button>
            <h2 class="text-2xl font-bold">{{ year }}年</h2>
            <button @click="year++" class="p-2 bg-white rounded shadow">
                <svg xmlns="http://www.w3.org/2000/svg" class="h-10 w-10 rotate-180" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path stroke-linecap="round" stroke-linejoin="round" stroke-width="2" d="M15 19l-7-7 7-7" />
                </svg>
            </button>
        </div>

        <!-- カレンダー -->
        <div class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-6 gap-6 px-6">
            <div v-for="(img, i) in images" :key="i" class="bg-white rounded-lg shadow overflow-hidden">
                <!-- 写真 -->
                <img :src="img" class="w-full aspect-[4/3] h-40 object-cover transition-opacity duration-500" loading="lazy" />

                <!-- 月タイトル -->
                <h3 class="text-center text-lg font-bold py-2">{{ i + 1 }}月</h3>

                <!-- カレンダー -->
                <div class="px-4 pb-4">
                    <!-- 曜日 -->
                    <div class="grid grid-cols-7 text-xs font-bold text-center text-gray-500 mb-1">
                        <span v-for="w in week" :key="w">{{ w }}</span>
                    </div>

                    <!-- 日付 -->
                    <div class="grid grid-cols-7 text-center text-sm">
                        <!-- 前空白を追加 -->
                        <span v-for="n in new Date(year, i, 1).getDay()" :key="'empty-' + n" class="p-1"></span>

                        <!-- 日付を出力 -->
                        <span
                            v-for="d in getDays(i + 1)"
                            :key="d"
                            class="relative group p-1 rounded cursor-pointer"
                            :class="{
                                'text-red-500': new Date(year, i, d).getDay() === 0, // 日曜
                                'text-blue-500': new Date(year, i, d).getDay() === 6, // 土曜
                                'font-bold bg-yellow-200 rounded-full': holidays[`${year}-${String(i + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`],
                                'bg-red-500 text-white rounded-full ring-2 ring-red-300': year === new Date().getFullYear() && i === new Date().getMonth() && d === new Date().getDate(),
                            }"
                        >
                            {{ d }}

                            <!-- 吹き出し -->
                            <div v-if="holidays[`${year}-${String(i + 1).padStart(2, '0')}-${String(d).padStart(2, '0')}`]" class="absolute bottom-full left-0 mb-2 hidden group-hover:block px-2 py-1 text-xs text-white bg-gray-800 rounded shadow" style="writing-mode: vertical-lr; text-orientation: upright">
                                {{ holidays[`${year}-${String(i + 1).padStart(2, "0")}-${String(d).padStart(2, "0")}`] }}
                            </div>
                            <div v-if="year === new Date().getFullYear() && i === new Date().getMonth() && d === new Date().getDate()" class="absolute bottom-full left-0 mb-2 hidden group-hover:block px-2 py-1 text-xs text-white bg-gray-800 rounded shadow" style="writing-mode: vertical-lr; text-orientation: upright">今日</div>
                        </span>
                    </div>
                </div>
            </div>
        </div>
    </main>
</template>
