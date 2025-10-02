<script setup lang="ts">
useHead({
    title: "Ginotoro Portfolio | ソフトウェアエンジニアの作品集",
    meta: [
        { name: "description", content: "Spring Boot × Nuxt3 × AWS を用いたソフトウェア開発ポートフォリオ。DB設計からCI/CDまで一貫して実装。" },
        { property: "og:title", content: "Ginotoro Portfolio" },
        { property: "og:description", content: "Spring Boot × Nuxt3 × AWS を用いたソフトウェア開発ポートフォリオ。" },
        { property: "og:image", content: "https://ginotoro-portfolio.jp/ogp/home.webp" },
        { property: "og:url", content: "https://ginotoro-portfolio.jp/" },
        { name: "twitter:card", content: "summary_large_image" },
    ],
    link: [{ rel: "canonical", href: "https://ginotoro-portfolio.jp/" }],
});
const skillGroups = [
    {
        title: "Frontend",
        skills: [
            { name: "Thymeleaf", years: 3, level: 3, icon: "devicon-thymeleaf-plain colored", note: "業務でのWEBアプリ開発経験あり<br />自走可能レベル" },
            { name: "jQuery", years: 3, level: 3, icon: "devicon-jquery-plain colored", note: "業務でのWEBアプリ開発経験あり<br />自走可能レベル" },
            { name: "Nuxt", years: 1, level: 2, icon: "devicon-nuxt-plain colored", note: "個人開発経験あり（spring-bootとのSSR対応）" },
            { name: "TailwindCSS", years: 1, level: 1, icon: "devicon-tailwindcss-plain colored", note: "AI活用しての個人開発経験あり" },
        ],
    },
    {
        title: "Backend",
        skills: [
            { name: "Java", years: 4, level: 3, icon: "devicon-java-plain colored", note: "業務でのWEBアプリ・バッチ・API開発経験あり<br />自走可能レベル" },
            { name: "SpringBoot", years: 4, level: 3, icon: "devicon-spring-plain colored", note: "業務でのWEBアプリ・バッチ・API開発経験あり<br />自走可能レベル" },
            { name: "Python", years: 1, level: 2, icon: "devicon-python-plain colored", note: "個人での学習レベル" },
        ],
    },
    {
        title: "Database",
        skills: [
            { name: "Oracle", years: 6, level: 4, icon: "devicon-oracle-original colored", note: "ExaやSE環境のDBA担当経験あり" },
            { name: "MySQL", years: 1, level: 1, icon: "devicon-mysql-plain colored", note: "個人開発で利用レベル" },
            // { name: "PostgreSQL", years: 3, level: 4, icon: "devicon-postgresql-plain colored", note: "業務システムで利用" },
        ],
    },
    {
        title: "Others",
        skills: [
            { name: "Linux", years: 6, level: 4, icon: "devicon-linux-plain colored", note: "サーバ構築・運用" },
            { name: "Docker", years: 3, level: 3, icon: "devicon-docker-plain colored", note: "コンテナ技術を利用したチーム開発や個人開発経験あり" },
            { name: "Git", years: 3, level: 3, icon: "devicon-git-plain colored", note: "チーム開発で必須" },
            { name: "GitHub", years: 3, level: 3, icon: "devicon-github-original colored", note: "CI/CD連携も経験" },
            { name: "AWS", years: 1, level: 1, icon: "devicon-amazonwebservices-plain-wordmark colored", note: "EC2などを個人開発で利用経験あり" },
        ],
    },
];

// 星を描画する関数
const renderStars = (level: number) => {
    return "★★★★★☆☆☆☆☆".slice(5 - level, 10 - level);
};

const { message } = useFlashMessage();

watch(message);
</script>

<template>
    <!-- Main -->
    <main class="flex-grow bg-gray-100">
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

        <!-- Hero -->
        <section class="py-20 bg-gray-300">
            <div class="container mx-auto px-6 text-center">
                <h2 class="text-4xl font-bold text-gray-800">Welcome to Ginotoro Portfolio</h2>
                <p class="mt-4 text-paleGray text-gray-600">
                    エンジニア Ginotoro のポートフォリオサイトです<br />
                    お忙しい中、お越しいただきありがとうございます<br />
                    よき休憩になりましたら、幸いです
                </p>
            </div>
        </section>

        <!-- About -->
        <section id="about" class="py-16 bg-gray-100">
            <div class="container mx-auto px-6 text-center">
                <h3 class="text-2xl font-bold text-gray-800">About Me</h3>
                <p class="mt-4 text-gray-600">
                    大学卒業後、Webアプリ開発やDBチューニングを中心に活動しています<br />
                    愛犬チュータ(ヨークシャテリア)をアイコンに使用しております<br />
                </p>
            </div>
        </section>

        <!-- Skills -->
        <section id="skills" class="py-16 bg-white">
            <div class="container mx-auto px-6 text-center">
                <h3 class="text-2xl font-bold text-gray-800">Skills</h3>
                <div class="mt-4 gap-6 grid xl:grid-cols-4 lg:grid-cols-2">
                    <div v-for="group in skillGroups" :key="group.title" class="p-6 bg-gray-50 rounded-lg shadow hover:shadow-md transition">
                        <h4 class="font-semibold text-lg mb-4 text-center">{{ group.title }}</h4>
                        <ul>
                            <li v-for="skill in group.skills" :key="skill.name" class="mb-3">
                                <span class="flex items-center relative group cursor-pointer">
                                    <i :class="`${skill.icon} text-2xl mr-1`"></i>
                                    <span class="flex-1 text-gray-600">{{ skill.name }}</span>
                                    <span class="flex-1 text-gray-600 text-sm mr-2 w-8">{{ skill.years }}年</span>
                                    <span class="text-yellow-400 text-base font-mono">
                                        <span v-for="i in 5" :key="i"> <span v-if="i <= skill.level">★</span><span v-else>☆</span> </span>
                                    </span>

                                    <span v-if="skill.note" class="absolute left-1/2 -translate-x-1/2 bottom-full mb-2 w-max max-w-xs px-3 py-2 rounded bg-gray-800 text-white text-xs opacity-0 group-hover:opacity-100 pointer-events-none transition-opacity z-10 shadow-lg">
                                        {{ skill.note }}
                                    </span>
                                </span>
                            </li>
                        </ul>
                    </div>
                </div>
            </div>
        </section>

        <!-- Works -->
        <section id="works" class="py-16 bg-gray-100">
            <div class="container mx-auto px-6 text-center">
                <h3 class="text-2xl font-bold text-gray-800">Works</h3>
                <div class="mt-4 grid gap-6 xl:grid-cols-3 md:grid-cols-2">
                    <div class="bg-white rounded-lg shadow hover:shadow-md transition overflow-hidden">
                        <NuxtLink to="/login" target="_blank" class="block hover:bg-gray-50 transition">
                            <img src="/login.webp" alt="Work1" class="w-full h-40 object-cover" />
                            <div class="p-4">
                                <h4 class="font-bold">JWT + Refresh Tokenを利用したログイン機能</h4>
                                <p class="text-gray-600 text-sm mt-2">SSR(nuxt + spring-boot)を利用して作成<br />※コードは、フッターよりGitHub参照</p>
                            </div>
                        </NuxtLink>
                    </div>

                    <div class="bg-white rounded-lg shadow hover:shadow-md transition overflow-hidden">
                        <a href="https://qiita.com/ginotoro/private/89c9cf5e73373c4baf37" target="_blank" class="block hover:bg-gray-50 transition">
                            <img
                                src="https://qiita-user-contents.imgix.net/https%3A%2F%2Fqiita-user-contents.imgix.net%2Fhttps%253A%252F%252Fcdn.qiita.com%252Fassets%252Fpublic%252Farticle-ogp-background-afbab5eb44e0b055cce1258705637a91.png%3Fixlib%3Drb-4.0.0%26w%3D1200%26blend64%3DaHR0cHM6Ly9xaWl0YS11c2VyLXByb2ZpbGUtaW1hZ2VzLmltZ2l4Lm5ldC9odHRwcyUzQSUyRiUyRnMzLWFwLW5vcnRoZWFzdC0xLmFtYXpvbmF3cy5jb20lMkZxaWl0YS1pbWFnZS1zdG9yZSUyRjAlMkYzODIyMjI1JTJGNWU2MGQxZjZhNTU5MTVmOTI3MzI4NzIxMWQ2NDA0MGUzZTgwYzRjOSUyRnhfbGFyZ2UucG5nJTNGMTc0NTk3NTc5Nj9peGxpYj1yYi00LjAuMCZhcj0xJTNBMSZmaXQ9Y3JvcCZtYXNrPWVsbGlwc2UmYmc9RkZGRkZGJmZtPXBuZzMyJnM9YTQ0ODg3MmZlMzdjOWU0MzdhY2Y4NmRhMWU4NjY4NDY%26blend-x%3D120%26blend-y%3D467%26blend-w%3D82%26blend-h%3D82%26blend-mode%3Dnormal%26s%3D9680ee3d84d274f25ec7713d42750887?ixlib=rb-4.0.0&w=1200&fm=jpg&mark64=aHR0cHM6Ly9xaWl0YS11c2VyLWNvbnRlbnRzLmltZ2l4Lm5ldC9-dGV4dD9peGxpYj1yYi00LjAuMCZ3PTk2MCZoPTMyNCZ0eHQ9JUUzJTgwJTkwT3JhY2xlJUUzJTgwJTkxREJBJUU2JThCJTg1JUU1JUJEJTkzJUUzJTgxJUE4JUUzJTgxJTk3JUUzJTgxJUE2JUUzJTgxJUFFJUU1JTgyJTk5JUU1JUJGJTk4JUU5JThDJUIyJUVGJUJDJTg4U1FMJUUzJTgyJTg0JUUzJTgyJUIzJUUzJTgzJTlFJUUzJTgzJUIzJUUzJTgzJTg5JUU5JTlCJTg2JUVGJUJDJTg5JnR4dC1hbGlnbj1sZWZ0JTJDdG9wJnR4dC1jb2xvcj0lMjMxRTIxMjEmdHh0LWZvbnQ9SGlyYWdpbm8lMjBTYW5zJTIwVzYmdHh0LXNpemU9NTYmdHh0LXBhZD0wJnM9NDM5NjczMzVkYzZjYmZmNWI5NDUwMDEwZjkyZjA5MzI&mark-x=120&mark-y=112&blend64=aHR0cHM6Ly9xaWl0YS11c2VyLWNvbnRlbnRzLmltZ2l4Lm5ldC9-dGV4dD9peGxpYj1yYi00LjAuMCZ3PTgzOCZoPTU4JnR4dD0lNDBnaW5vdG9ybyZ0eHQtY29sb3I9JTIzMUUyMTIxJnR4dC1mb250PUhpcmFnaW5vJTIwU2FucyUyMFc2JnR4dC1zaXplPTM2JnR4dC1wYWQ9MCZzPWU4ZTA5NDI3Y2JlNWE0Nzk5ZDA1MjA2YjE5MjVjMjk2&blend-x=242&blend-y=480&blend-w=838&blend-h=46&blend-fit=crop&blend-crop=left%2Cbottom&blend-mode=normal&s=7aaf7c20bcc60e5acca8eb826c36e1c6"
                                alt="【Oracle】DBA担当としての備忘録（SQLやコマンド集）"
                                class="w-full h-40 object-cover"
                            />
                            <div class="p-4">
                                <h4 class="font-bold">【Oracle】DBA担当としての備忘録（SQLやコマンド集）</h4>
                                <p class="text-gray-600 text-sm mt-2">これまでDBA担当として、積んだ知識を集約しています</p>
                            </div>
                        </a>
                    </div>

                    <div class="bg-white rounded-lg shadow hover:shadow-md transition overflow-hidden">
                        <NuxtLink to="/chuta" target="_blank" class="block hover:bg-gray-50 transition">
                            <img src="/chuta/chuta.webp" alt="Work1" class="w-full h-40 object-cover" />
                            <div class="p-4">
                                <h4 class="font-bold">愛犬のカレンダー</h4>
                                <p class="text-gray-600 text-sm mt-2">nuxt + tailwindcssで制作<br />※AI任せ</p>
                            </div>
                        </NuxtLink>
                    </div>
                </div>
            </div>
        </section>
    </main>
</template>
