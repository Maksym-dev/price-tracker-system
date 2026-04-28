import React, { useState } from 'react';
import { useQuery } from '@tanstack/react-query';
import { productService } from '../services/api';

export const AddProductForm = ({ onAdd }) => {
  const [url, setUrl] = useState('');
  const [target, setTarget] = useState('');

  // Загрузка списка доступных товаров из REST сервиса
  const { data, isLoading, isError } = useQuery({
    queryKey: ['availableProducts'],
    queryFn: productService.getProducts,
  });

  const products = data?.content || [];

  const handleSubmit = (e) => {
    e.preventDefault();
    if (!url) return;

    onAdd({ url, targetPrice: parseFloat(target) });
    setUrl('');
    setTarget('');
  };

  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
      <h2 className="text-lg font-semibold mb-4 text-gray-800">Добавить товар для отслеживания</h2>
      <form onSubmit={handleSubmit} className="flex flex-col md:flex-row gap-4">
        {/* Выпадающий список вместо текстового поля URL */}
        <select
          className="flex-1 p-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none bg-white"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          disabled={isLoading}
          required
        >
          <option value="" disabled>
            {isLoading ? 'Загрузка товаров...' : 'Выберите товар из списка'}
          </option>
          {/* Безопасный перебор массива с использованием опциональной цепочки */}
          {products?.map((product) => (
            <option key={product.id} value={product.url}>
              {product.title}
            </option>
          ))}
        </select>

        <input
          type="number"
          placeholder="Желаемая цена"
          className="w-full md:w-40 p-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
          value={target}
          onChange={(e) => setTarget(e.target.value)}
          required
        />
        <button className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-6 rounded-lg transition-colors disabled:bg-gray-400" disabled={isLoading || isError}>
          Отслеживать
        </button>
      </form>
      {isError && (
        <p className="text-red-500 text-sm mt-2">Ошибка при загрузке списка товаров.</p>
      )}
    </div>
  );
};