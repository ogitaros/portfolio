/** @type {import('tailwindcss').Config} */
export default {
  content: [
    './components/**/*.{vue,js,ts}',
    './layouts/**/*.vue',
    './pages/**/*.vue',
    './app.vue',
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          bg: '#6b7280',   // gray-500
          hover: '#212529', // gray-900
          text: '#FFFFFF' // 白
        },
      fontFamily: {
        sans: ["Graphik", "sans-serif"],
        serif: ["Merriweather", "serif"],
      },
      },
      spacing: {
        headerX: '2.5rem', // px-10 相当
        headerY: '1.25rem', // py-5 相当
        footerX: '0.75rem', // px-3 相当
        footerY: '0.75rem', // py-3 相当
      },
    },
    // フォントサイズ設定
    fontSize: {
      xs: ["0.75rem", { lineHeight: "1rem", letterSpacing: "0.03em" }],
      sm: ["0.875rem", { lineHeight: "1.25rem", letterSpacing: "0.03em" }],
      base: ["1rem", { lineHeight: "1.5rem", letterSpacing: "0.03em" }],
      lg: ["1.125rem", { lineHeight: "1.75rem", letterSpacing: "0.03em" }],
      xl: ["1.25rem", { lineHeight: "1.75rem", letterSpacing: "0.03em" }],
      "2xl": ["1.5rem", { lineHeight: "2rem", letterSpacing: "0.03em" }],
      "3xl": ["1.875rem", { lineHeight: "2.25rem", letterSpacing: "0.03em" }],
      "4xl": ["2.25rem", { lineHeight: "2.5rem", letterSpacing: "0.03em" }],
      "5xl": ["3rem", { lineHeight: "1", letterSpacing: "0.03em" }],
    },
  },

  plugins: [],
}
