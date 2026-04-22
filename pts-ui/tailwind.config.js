/** @type {import('tailwindcss').Config} */
module.exports = {
  content: [
    "./index.html",
    "./src/**/*.{js,jsx,ts,tsx}",
  ],
  theme: {
    extend: {
      colors: {
        brand: {
          light: '#3b82f6',
          dark: '#1e3a8a',
        }
      }
    },
  },
  plugins: [],
}