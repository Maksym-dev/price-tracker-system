import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { useTranslation } from 'react-i18next';
import { productService } from '../services/api';

export const AddProductForm = ({ onAdd }) => {
  const { t } = useTranslation();

  const [selectedUrl, setSelectedUrl] = useState('');
  const [targetPrice, setTargetPrice] = useState('');

  // Состояние текущей страницы (начинаем с 0 или 1, зависит от вашего API)
  const [page, setPage] = useState(0); 
  const pageSize = 10;

  // Загрузка списка доступных товаров из REST сервиса
  const { data, isLoading, isError, isPlaceholderData } = useQuery({
    queryKey: ['availableProducts', page],
    queryFn: () => productService.getProducts(page, pageSize),
    placeholderData: (previousData) => previousData, // Чтобы интерфейс не "мигал" при загрузке новой страницы
  });

  const products = data?.content || [];

  const productsPage = data?.page || {};

  const totalPages = productsPage?.totalPages || 0; // Общее кол-во страниц от сервера

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!selectedUrl || !targetPrice) return;
    
    onAdd({ url: selectedUrl, targetPrice: parseFloat(targetPrice) });
    setSelectedUrl('');
    setTargetPrice('');
  };

return (
    <div className="bg-white p-6 rounded-xl shadow-md border border-gray-100 mb-8">
      <h2 className="text-xl font-bold mb-6 text-gray-800 text-center">{t('select_product_title')}</h2>
      
      {isLoading ? (
        <div className="flex justify-center py-10">
          <div className="animate-spin rounded-full h-8 w-8 border-b-2 border-blue-600"></div>
        </div>
      ) : isError ? (
        <p className="text-center py-10 text-red-500">Ошибка загрузки данных</p>
      ) : (
        <>
          {/* Сетка карточек (теперь данные приходят порциями) */}
          <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-4 mb-6">
            {products.map((product) => (
              <div 
                key={product.id}
                onClick={() => setSelectedUrl(product.url)}
                className={`cursor-pointer p-4 rounded-lg border-2 transition-all ${
                  selectedUrl === product.url 
                    ? 'border-blue-500 bg-blue-50' 
                    : 'border-gray-100 hover:border-blue-200'
                }`}
              >
                <h3 className="font-semibold text-gray-700">{product.title}</h3>
                <p className="text-xs text-gray-400 mt-1 truncate">{product.url}</p>
              </div>
            ))}
          </div>

          {/* Кнопки переключения страниц через API */}
          <div className="flex justify-center items-center gap-4 mb-8">
            <button 
              onClick={() => setPage(old => Math.max(old - 1, 0))}
              disabled={page === 0}
              className="px-4 py-2 bg-gray-100 rounded-lg disabled:opacity-50 hover:bg-gray-200 transition-colors"
            >
              ← {t('back_pagination_button')}
            </button>
            
            <span className="font-medium text-gray-600">
              {t('page_title')} {page + 1} {t('page_of_title')} {totalPages}
            </span>

            <button 
              onClick={() => {
                if (!data?.last) { // Если сервер говорит, что это не последняя страница
                  setPage(old => old + 1);
                }
              }}
              disabled={data?.last || page + 1 >= totalPages}
              className="px-4 py-2 bg-gray-100 rounded-lg disabled:opacity-50 hover:bg-gray-200 transition-colors"
            >
              {t('next_pagination_button')} →
            </button>
          </div>
        </>
      )}

      {/* Форма подтверждения */}
      <form onSubmit={handleSubmit} className="flex flex-col sm:flex-row gap-4 items-end border-t pt-6">
        <div className="flex-1 w-full">
          <label className="block text-sm font-medium text-gray-600 mb-1">{t('selected_url_title')}</label>
          <input
            type="text"
            readOnly
            value={selectedUrl}
            className="w-full p-3 bg-gray-50 border border-gray-200 rounded-lg text-gray-500 outline-none"
            placeholder={t('product_placeholder')}
          />
        </div>
        <div className="w-full sm:w-40">
          <label className="block text-sm font-medium text-gray-600 mb-1">{t('target_price')}</label>
          <input
            type="number"
            value={targetPrice}
            onChange={(e) => setTargetPrice(e.target.value)}
            required
            className="w-full p-3 border border-gray-200 rounded-lg outline-none focus:ring-2 focus:ring-blue-500"
          />
        </div>
        <button 
          type="submit"
          disabled={!selectedUrl}
          className="bg-blue-600 text-white py-3 px-8 rounded-lg font-bold hover:bg-blue-700 disabled:bg-gray-300"
        >
          {t('add_subscription')}
        </button>
      </form>
    </div>
  );
};