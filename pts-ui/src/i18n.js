import i18n from 'i18next';
import { initReactI18next } from 'react-i18next';
import LanguageDetector from 'i18next-browser-languagedetector';

// Файлы с переводами (можно вынести в отдельные JSON файлы)
const resources = {
  en: {
    translation: {
      "pts_ui_app_description": "Stay up-to-date with the best deals in real time",
      "api_error": "Data not found or API error occurred",
      "dashboard_title": "My Subscriptions",
      "add_product": "Add Product",
      "add_subscription": "Add subscription",
      "select_product_title": "Choose product",
      "loading": "Loading...",
      "logout": "Logout",
      "login": "Login",
      "current_price": "Current Price",
      "target_price": "Target Price",
      "stores_title": "Stores",
      "price_history_title": "Price history",
      "settings_title": "Settings",
      "back_pagination_button": "Back",
      "page_title": "Page",
      "page_of_title": "of",
      "next_pagination_button": "Next",
      "selected_url_title": "Selected URL",
      "product_placeholder": "Click on the card above",
      "target_price_reached": "Price reached",
      "target_price_in_search": "In search",
      "show_price_history": "View price history",
      "rights_footer": "All rights reserved.",
      "help_footer": "Help",
      "privacy_footer": "Privacy",
      "no_data_for_chart": "There is no data to plot the graph.",
      "password_title": "Password",
      "login_in_title": "Login in",
      "back_to_subscriptions": "Back to subscriptions",
      "price_report_description": "Detailed report on price changes over the last 30 days",
      "min": "Minimum",
      "max": "Maximum",
      "difference": "Difference",
      "chart_title": "Price dynamics"
    }
  },
  ru: {
    translation: {
      "pts_ui_app_description": "Следите за лучшими предложениями в реальном времени",
      "api_error": "Данные не найдены или произошла ошибка API",
      "dashboard_title": "Мои подписки",
      "add_product": "Добавить товар",
      "add_subscription": "Добавить подписку",
      "select_product_title": "Выберите товар",
      "loading": "Загрузка...",
      "logout": "Выйти",
      "login": "Войти",
      "current_price": "Текущая цена",
      "target_price": "Целевая цена",
      "stores_title": "Магазины",
      "price_history_title": "История цен",
      "settings_title": "Настройки",
      "back_pagination_button": "Назад",
      "page_title": "Страница",
      "page_of_title": "из",
      "next_pagination_button": "Вперед",
      "selected_url_title": "Выбранный URL",
      "product_placeholder": "Кликните на карточку выше",
      "target_price_reached": "Цена достигнута",
      "target_price_in_search": "В поиске",
      "show_price_history": "Посмотреть историю цен",
      "rights_footer": "Все права защищены.",
      "help_footer": "Помощь",
      "privacy_footer": "Конфиденциальность",
      "no_data_for_chart": "Нет данных для построения графика",
      "password_title": "Пароль",
      "login_in_title": "Вход в",
      "back_to_subscriptions": "Вернуться к подпискам",
      "price_report_description": "Детальный отчет об изменении цены за последние 30 дней",
      "min": "Минимум",
      "max": "Максимум",
      "difference": "Разница",
      "chart_title": "Динамика стоимости"
    }
  }
};

i18n
  .use(LanguageDetector) // Автоматическое определение языка браузера
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: 'en',
    interpolation: {
      escapeValue: false
    }
  });

export default i18n;