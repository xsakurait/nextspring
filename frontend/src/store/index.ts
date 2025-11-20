import { create } from 'zustand';
import type { User, Channel, Video, Category } from '@/types';

interface AuthState {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  login: (token: string, username: string, email: string) => void;
  logout: () => void;
}

export const useAuthStore = create<AuthState>((set) => ({
  user: null,
  token: null,
  isAuthenticated: false,
  login: (token, username, email) => {
    localStorage.setItem('token', token);
    set({
      token,
      user: { id: 0, username, email },
      isAuthenticated: true,
    });
  },
  logout: () => {
    localStorage.removeItem('token');
    set({ user: null, token: null, isAuthenticated: false });
  },
}));

interface ChannelState {
  channels: Channel[];
  selectedCategory: number | null;
  setChannels: (channels: Channel[]) => void;
  setSelectedCategory: (categoryId: number | null) => void;
}

export const useChannelStore = create<ChannelState>((set) => ({
  channels: [],
  selectedCategory: null,
  setChannels: (channels) => set({ channels }),
  setSelectedCategory: (categoryId) => set({ selectedCategory: categoryId }),
}));

interface VideoState {
  videos: Video[];
  sortBy: 'publishedAt' | 'viewCount' | 'likeCount';
  days: number;
  setVideos: (videos: Video[]) => void;
  setSortBy: (sortBy: 'publishedAt' | 'viewCount' | 'likeCount') => void;
  setDays: (days: number) => void;
}

export const useVideoStore = create<VideoState>((set) => ({
  videos: [],
  sortBy: 'publishedAt',
  days: 7,
  setVideos: (videos) => set({ videos }),
  setSortBy: (sortBy) => set({ sortBy }),
  setDays: (days) => set({ days }),
}));

interface CategoryState {
  categories: Category[];
  setCategories: (categories: Category[]) => void;
}

export const useCategoryStore = create<CategoryState>((set) => ({
  categories: [],
  setCategories: (categories) => set({ categories }),
}));
