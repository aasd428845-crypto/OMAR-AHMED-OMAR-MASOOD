export interface Restaurant {
  id: string;
  nameAr: string;
  nameEn: string;
  coverImage: string;
  logoUrl: string;
  rating: number;
  estimatedDeliveryTime: number;
  deliveryFee: number;
  isActive: boolean;
  category: string;
}

export interface MenuItem {
  id: string;
  nameAr: string;
  nameEn: string;
  description: string;
  price: number;
  originalPrice?: number;
  imageUrl: string;
  categoryId: string;
  restaurantId: string;
  isAvailable: boolean;
  isPopular: boolean;
}

export interface MenuCategory {
  id: string;
  nameAr: string;
  nameEn: string;
  image: string;
}

export interface CartItem {
  menuItem: MenuItem;
  quantity: number;
  restaurantId: string;
  restaurantName: string;
}

export interface DeliveryBanner {
  id: string;
  title: string;
  subtitle?: string;
  imageUrl: string;
  badgeText?: string;
  restaurantId?: string;
}

export interface Order {
  id: string;
  restaurantName: string;
  items: { name: string; quantity: number; price: number }[];
  totalPrice: number;
  date: string;
  status: 'pending' | 'preparing' | 'picked_up' | 'on_the_way' | 'delivered';
}

export interface KotlinFile {
  name: string;
  path: string;
  description: string;
  content: string;
}
