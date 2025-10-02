// https://nuxt.com/docs/api/configuration/nuxt-config
export default defineNuxtConfig({

  compatibilityDate: '2025-07-15',

  modules: ["@nuxtjs/tailwindcss", '@nuxtjs/sitemap'],


  app: {
    head: {
      titleTemplate: '%s | Ginotoro Portfolio',
      meta: [
        { name: 'description', content: 'Ginotoroのポートフォリオサイトです。DB・Spring Boot・Nuxtの知見をまとめています。' },
        { property: 'og:type', content: 'website' },
        { property: 'og:site_name', content: 'Ginotoro Portfolio' },
        { name: 'twitter:card', content: 'summary_large_image' }
      ],
      link: [
        { rel: 'icon', type: 'image/x-icon', href: '/ginotoro_favicon.ico' }
      ],
    },
  },

  sitemap: {
    hostname: process.env.NODE_ENV === 'production'
      ? 'https://ginotoro-portfolio.jp'
      : 'http://localhost:8080',
    exclude: ['/login', '/mypage', '/signup'],
  },

  devtools: { enabled: true },


  postcss: {
    plugins: {
      tailwindcss: {},
      autoprefixer: {},

    },
  },
  sourcemap: {
    server: true,
    client: true
  },
  runtimeConfig: {
    public: {
      apiBase: process.env.API_BASE || (
        process.env.NODE_ENV === 'production'
          ? 'https://ginotoro-portfolio.jp/api'
          : 'http://localhost:8080/api'
      )
    },
  },
})
