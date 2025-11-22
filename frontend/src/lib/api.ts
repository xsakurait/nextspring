import axios from 'axios';
import type {
  AuthResponse,
  LoginRequest,
  SignupRequest,
  Channel,
  Video,
  Category,
} from '@/types';

const API_URL =
  typeof window === 'undefined'
    ? process.env.SERVER_API_URL || 'http://backend:8080/api'
    : process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080/api';

const api = axios.create({
  baseURL: API_URL,
  headers: {
    'Content-Type': 'application/json',
  },
});

// リクエストインターセプター：JWTトークンを追加
api.interceptors.request.use(
  (config) => {
    if (typeof window !== 'undefined') {
      const token = localStorage.getItem('token');
      if (token) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => {
    return Promise.reject(error);
  }
);

// レスポンスインターセプター：401エラーでログアウト
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (typeof window !== 'undefined' && error.response?.status === 401) {
      localStorage.removeItem('token');
      window.location.href = '/login';
    }
    return Promise.reject(error);
  }
);

// 認証API
export const authApi = {
  login: async (data: LoginRequest): Promise<AuthResponse> => {
    const response = await api.post<AuthResponse>('/auth/login', data);
    return response.data;
  },

  signup: async (data: SignupRequest): Promise<string> => {
    const response = await api.post<string>('/auth/signup', data);
    return response.data;
  },
};

// チャンネルAPI
export const channelApi = {
  getAll: async (): Promise<Channel[]> => {
    const response = await api.get<Channel[]>('/channels');
    return response.data;
  },

  getByCategory: async (categoryId: number): Promise<Channel[]> => {
    const response = await api.get<Channel[]>(
      `/channels/category/${categoryId}`
    );
    return response.data;
  },

  subscribe: async (
    channelId: string,
    categoryId?: number
  ): Promise<Channel> => {
    const response = await api.post<Channel>('/channels/subscribe', {
      channelId,
      categoryId,
    });
    return response.data;
  },

  unsubscribe: async (channelId: number): Promise<void> => {
    await api.delete(`/channels/${channelId}`);
  },

  updateCategory: async (
    channelId: number,
    categoryId: number
  ): Promise<void> => {
    await api.put(`/channels/${channelId}/category`, { categoryId });
  },
};

// 動画API
export const videoApi = {
  getRecent: async (days: number = 7, limit: number = 50): Promise<Video[]> => {
    const response = await api.get<Video[]>('/videos/recent', {
      params: { days, limit },
    });
    return response.data;
  },

  getRecentByCategory: async (
    categoryId: number,
    days: number = 7,
    limit: number = 50
  ): Promise<Video[]> => {
    const response = await api.get<Video[]>(
      `/videos/recent/category/${categoryId}`,
      {
        params: { days, limit },
      }
    );
    return response.data;
  },

  getRecentSorted: async (
    days: number = 7,
    sortBy: string = 'publishedAt',
    limit: number = 50
  ): Promise<Video[]> => {
    const response = await api.get<Video[]>('/videos/recent/sorted', {
      params: { days, sortBy, limit },
    });
    return response.data;
  },

  sync: async (): Promise<void> => {
    await api.post('/videos/sync');
  },

  updateStats: async (videoId: string): Promise<Video> => {
    const response = await api.put<Video>(`/videos/${videoId}/update-stats`);
    return response.data;
  },
};

// カテゴリAPI
export const categoryApi = {
  getAll: async (): Promise<Category[]> => {
    const response = await api.get<Category[]>('/categories');
    return response.data;
  },

  create: async (data: Partial<Category>): Promise<Category> => {
    const response = await api.post<Category>('/categories', data);
    return response.data;
  },

  update: async (id: number, data: Partial<Category>): Promise<Category> => {
    const response = await api.put<Category>(`/categories/${id}`, data);
    return response.data;
  },

  delete: async (id: number): Promise<void> => {
    await api.delete(`/categories/${id}`);
  },
};

export default api;
