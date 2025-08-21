# 🛒 **Smart Grocery Store** - Your Ultimate Shopping Companion 🛒

![Smart Grocery Store Logo](https://images.unsplash.com/photo-1542838132-92c53300491e?w=200&h=200&fit=crop&crop=center&mask=circle&maxage=7d)

## 📱 **About the App**

Welcome to **Smart Grocery Store**! 🎉 Your one-stop solution for fresh groceries and household essentials. Whether you're looking to stock up on fresh produce, grab your daily necessities, or discover amazing deals, **Smart Grocery Store** makes shopping as easy as a few taps on your phone! 🥬🥛🍎

I've designed this app to make grocery shopping effortless - browse products, add favorites, place orders, and track everything in one beautiful, user-friendly interface. No more long queues or forgotten shopping lists! 🛍️

### ✨ **Key Features**

- 🛍️ **Easy Shopping**: Browse through categorized products with detailed information, prices, and stock status
- 🔍 **Smart Search & Filter**: Find exactly what you need with powerful search and category filters
- ❤️ **Favorites System**: Save your favorite products for quick reordering
- 🛒 **Order Management**: Place orders with pickup or delivery options and track their status
- 💰 **Special Offers**: Never miss a deal with our dedicated offers section
- 👤 **User Profiles**: Manage your account, update information, and view order history
- 🏪 **Admin Panel**: Complete administrative control for managing products, users, and orders
- 📱 **Offline Support**: SQLite database ensures functionality even without internet connection

## 🎯 **Project Information**

**Course**: ENCS5150 – Advanced Computer Systems Engineering Laboratory  
**Institution**: Birzeit University  
**Team**: Two-student collaboration project  
**Academic Year**: First Summer Semester - 2025

## 📸 **Screenshots**

### **Authentication & Onboarding**
<img src="path/to/login_screenshot.png" width="200" height="400" alt="Login Screen" />
<img src="path/to/register_screenshot.png" width="200" height="400" alt="Registration Screen" />

*Secure login and registration with remember me functionality and comprehensive input validation*

### **Core Shopping Experience**
<img src="path/to/products_screenshot.png" width="200" height="400" alt="Products Screen" />
<img src="path/to/orders_screenshot.png" width="200" height="400" alt="My Orders Screen" />
<img src="path/to/favorites_screenshot.png" width="200" height="400" alt="My Favorites Screen" />

*Browse products, manage orders, and save favorites with beautiful, intuitive interfaces*

### **Special Features**
<img src="path/to/offers_screenshot.png" width="200" height="400" alt="Special Offers Screen" />

*Discover amazing deals and limited-time discounts*

## 🛠️ **Technical Architecture**

### **Built With**
- **Android Studio** - Primary IDE for development
- **Java** - Core programming language
- **SQLite Database** - Local data storage and offline functionality
- **Volley Library** - RESTful API integration and network operations
- **SharedPreferences** - User session management and preferences
- **Material Design Components** - Modern, beautiful UI components
- **Fragments** - Modular UI architecture for better navigation

### **Key Technical Features**
- 🗄️ **Hybrid Data Management**: Online API integration with offline SQLite fallback
- 🔐 **Security**: Encrypted password storage using Caesar cipher
- 🎨 **Animations**: Smooth transitions and interactive feedback
- 📱 **Responsive Design**: Optimized for various screen sizes
- 🔄 **Real-time Sync**: Seamless data synchronization between online and offline modes

## 🚀 **Getting Started**

### **Prerequisites**
- **Android Studio** (Latest version recommended)
- **Android SDK** with API Level 26 minimum
- **Java Development Kit (JDK) 11** or higher
- **Git** for version control

### **Installation**
1. **Clone the repository**:
   ```bash
   git clone https://github.com/lamaRimawi/android_app_project.git
   ```

2. **Open in Android Studio**:
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory

3. **Configure the project**:
   - Ensure Android SDK is properly configured
   - Sync project with Gradle files
   - Verify all dependencies are downloaded

4. **Run the application**:
   - Connect an Android device or start an emulator
   - Build and run the project
   - Test with the pre-configured admin account: `admin@admin.com` / `Admin123!`

### **API Configuration**
The app uses a mock API for product data. Default endpoint:
```
https://mocki.io/v1/cede0a18-239c-4370-a84f-93fd197c5111
```

## 📋 **Features Overview**

### **For Customers**
- ✅ **User Registration & Login** with comprehensive validation
- ✅ **Product Browsing** with search and category filters
- ✅ **Shopping Cart** functionality with quantity management
- ✅ **Order Placement** with pickup/delivery options
- ✅ **Favorites Management** for quick reordering
- ✅ **Profile Management** with photo upload capability
- ✅ **Order Tracking** with real-time status updates
- ✅ **Contact Features** (Call, Map, Email integration)

### **For Administrators**
- ✅ **User Management** - View and delete customer accounts
- ✅ **Product Management** - Add, edit, and delete products
- ✅ **Order Management** - View all orders and update status
- ✅ **Admin Creation** - Add new administrative users
- ✅ **Analytics Dashboard** - Overview of store operations

## 🏗️ **Project Structure**

```
app/
├── src/main/java/com/grocery/store/a1213515_1200209/
│   ├── activities/          # All Activity classes
│   ├── fragments/          # Fragment implementations
│   ├── models/            # Data model classes
│   ├── database/          # SQLite database helper
│   ├── adapters/          # RecyclerView adapters
│   └── utils/             # Utility classes
├── src/main/res/
│   ├── layout/           # XML layout files
│   ├── drawable/         # Images and drawable resources
│   ├── values/           # Colors, strings, styles
│   └── anim/            # Animation resources
└── src/main/assets/      # Static assets
```

## 🔧 **Database Schema**

### **Users Table**
- User ID, Email, First Name, Last Name
- Encrypted Password, Gender, City, Phone
- Role (Customer/Admin)

### **Products Table**
- Product ID, Name, Category, Price
- Description, Image URL, Stock Quantity
- Creation/Update timestamps

### **Orders Table**
- Order ID, User ID, Product ID, Quantity
- Order Date, Status, Delivery Method
- Total Amount

### **Favorites Table**
- User ID, Product ID, Date Added

## 🎯 **Testing Information**

**Target Device**: Pixel 3a XL  
**API Level**: 26 (Android 8.0)  
**Graphics**: Software rendering  

Thoroughly tested on the specified device configuration to ensure optimal performance and compatibility.

## 🤝 **Contributing**

This is an academic project developed as part of the Advanced Computer Systems Engineering Laboratory course. While not open for external contributions, the code serves as a comprehensive example of Android application development.

### **Development Team**
- **Student ID**: 1213515
- **Student ID**: 1200209
- **Course**: ENCS5150
- **Institution**: Birzeit University

## 📱 **App Permissions**

- **INTERNET** - For API communication and online features
- **WRITE_EXTERNAL_STORAGE** - For image storage and caching
- **READ_EXTERNAL_STORAGE** - For accessing user-selected images
- **CALL_PHONE** - For direct calling functionality

## 🔐 **Security Features**

- Password encryption using Caesar cipher
- Input validation for all user inputs
- SQL injection prevention
- Secure session management
- Admin role verification

## 📊 **Performance Optimizations**

- Efficient database queries with proper indexing
- Image caching and loading optimization
- Network request optimization with Volley
- Memory management for large datasets
- Smooth animations with proper lifecycle management

## 🐛 **Known Issues & Limitations**

- App requires internet connection for initial product data fetch
- Image loading may be slower on low-bandwidth connections
- Admin panel requires manual database initialization

## 📞 **Support & Contact**

For questions about this academic project:
- 📧 **Email**: [Student Email]
- 🏫 **Institution**: Birzeit University
- 📚 **Course**: ENCS5150 - Advanced Computer Systems Engineering Laboratory

---

## 📄 **License**

This project is developed for educational purposes as part of the Advanced Computer Systems Engineering Laboratory course at Birzeit University. All rights reserved.

---

**Note**: This application was developed with attention to detail, modern Android development practices, and user experience principles. Every feature has been carefully implemented to meet the project requirements while maintaining code quality and performance standards. 🚀

---

⭐ **If you find this project helpful for your own Android development journey, please star the repository!** ⭐
