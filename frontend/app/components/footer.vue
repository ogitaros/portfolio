<script setup></script>

<template>
    <footer class="bg-brand-bg text-brand-text px-footerX py-footerY shadow">
        <div class="mx-auto flex justify-center gap-4">
            <!-- Mail -->
            <a href="mailto:syouta139@gmail.com?subject=お問い合わせ&body=ここに本文を書いてください">
                <img src="/logos/mail-icon.png" alt="mail" class="h-5 pt-1 px-4 hover:opacity-50 transition" />
            </a>

            <!-- GitLab -->
            <a href="https://github.com/ogitaros" target="_blank">
                <img src="/logos/gitlab-logo-700-rgb.svg" alt="GitLab" class="h-7 pb-1 px-4 hover:opacity-50 transition" />
            </a>

            <!-- Qiita -->
            <a href="https://qiita.com/ginotoro" target="_blank" rel="noopener noreferrer">
                <img src="/logos/qiita-white-icon.png" alt="Qiita" class="h-5 pt-1 px-4 hover:opacity-50 transition" />
            </a>
        </div>

        <div class="mx-auto flex justify-center">
            <p class="text-base">&copy; 2025 Ginotoro</p>
        </div>
    </footer>
</template>
