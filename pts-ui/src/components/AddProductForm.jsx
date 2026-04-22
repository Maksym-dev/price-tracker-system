import React, { useState } from 'react';

export const AddProductForm = ({ onAdd }) => {
  const [url, setUrl] = useState('');
  const [target, setTarget] = useState('');

  const handleSubmit = (e) => {
    e.preventDefault();
    onAdd({ url, targetPrice: parseFloat(target) });
    setUrl('');
    setTarget('');
  };

  return (
    <div className="bg-white p-6 rounded-xl shadow-sm border border-gray-100 mb-8">
      <h2 className="text-lg font-semibold mb-4 text-gray-800">Добавить товар для отслеживания</h2>
      <form onSubmit={handleSubmit} className="flex flex-col md:flex-row gap-4">
        <input
          type="url"
          placeholder="https://example.com/product..."
          className="flex-1 p-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
          value={url}
          onChange={(e) => setUrl(e.target.value)}
          required
        />
        <input
          type="number"
          placeholder="Желаемая цена"
          className="w-full md:w-40 p-3 border border-gray-200 rounded-lg focus:ring-2 focus:ring-blue-500 outline-none"
          value={target}
          onChange={(e) => setTarget(e.target.value)}
          required
        />
        <button className="bg-blue-600 hover:bg-blue-700 text-white font-medium py-3 px-6 rounded-lg transition-colors">
          Отслеживать
        </button>
      </form>
    </div>
  );
};