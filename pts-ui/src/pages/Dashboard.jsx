import React, { useState } from 'react';
import { AddProductForm } from '../components/AddProductForm';
import { ProductCard } from '../components/ProductCard';
import { useQuery, useMutation, useQueryClient } from '@tanstack/react-query';
import { subscriptionService } from '../services/api';

const Dashboard = () => {
  const queryClient = useQueryClient();

  // Получение данных
  const { data, isLoading } = useQuery({
    queryKey: ['subscriptions'],
    queryFn: subscriptionService.getSubscriptions
  });

  const subscriptions = data?.content || [];

  // Создание новой подписки
  const mutation = useMutation({
    mutationFn: subscriptionService.createSubscription,
    onSuccess: () => {
      // Обновляем список после успешного добавления
      queryClient.invalidateQueries({ queryKey: ['subscriptions'] });
    },
  });

  if (isLoading) return <div>Загрузка...</div>;

  if (!Array.isArray(subscriptions)) {
    return <div>Данные не найдены или произошла ошибка API</div>;
  }

  const handleAddProduct = (newSub) => {
    // Здесь будет вызов POST /subscriptions
    console.log('Отправка на бэкенд:', newSub);
  };

  return (
    <div className="min-h-screen bg-gray-50 py-8 px-4">
      <div className="max-w-6xl mx-auto">
        <header className="mb-10">
          <h1 className="text-3xl font-extrabold text-gray-900">Price Tracker</h1>
          <p className="text-gray-600">Следите за лучшими предложениями в реальном времени</p>
        </header>

        <AddProductForm onAdd={handleAddProduct} />

        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
          {subscriptions?.map(sub => (
            <ProductCard key={sub.id} product={sub} />
          ))}
        </div>
      </div>
    </div>
  );
};

export default Dashboard;