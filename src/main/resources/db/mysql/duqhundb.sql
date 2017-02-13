-- phpMyAdmin SQL Dump
-- version 4.5.1
-- http://www.phpmyadmin.net
--
-- Host: 127.0.0.1
-- Generation Time: Feb 13, 2017 at 06:23 AM
-- Server version: 10.1.16-MariaDB
-- PHP Version: 7.0.9

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";

--
-- Database: `duqhandb`
--

-- --------------------------------------------------------

--
-- Table structure for table `cart`
--

CREATE TABLE `cart` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `load_date` datetime NOT NULL,
  `sizecolormap_id` bigint(32) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `category`
--

CREATE TABLE `category` (
  `id` bigint(20) NOT NULL,
  `name` varchar(255) NOT NULL,
  `parent_id` bigint(20) NOT NULL,
  `parent_path` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `color`
--

CREATE TABLE `color` (
  `id` bigint(20) NOT NULL,
  `code` varchar(50) NOT NULL,
  `name` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `order_details`
--

CREATE TABLE `order_details` (
  `id` bigint(32) NOT NULL,
  `order_id` varchar(255) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `payment_key` varchar(50) NOT NULL,
  `map_id` bigint(32) NOT NULL,
  `payment_amount` double NOT NULL,
  `order_date` datetime NOT NULL,
  `status` varchar(50) NOT NULL,
  `quentity` bigint(32) NOT NULL,
  `address_id` bigint(32) NOT NULL,
  `delivery_date` datetime DEFAULT NULL,
  `shipment_id` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `otp_table`
--

CREATE TABLE `otp_table` (
  `id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `user_mail` varchar(255) NOT NULL,
  `otp` varchar(50) NOT NULL,
  `send_time` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `payment_detail`
--

CREATE TABLE `payment_detail` (
  `id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `payment_key` varchar(50) NOT NULL,
  `payer_id` varchar(50) DEFAULT NULL,
  `pay_amount` double NOT NULL,
  `payment_type` varchar(20) NOT NULL,
  `payment_date` date NOT NULL,
  `payment_status` varchar(10) NOT NULL,
  `payment_account` varchar(255) DEFAULT NULL,
  `access_token` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `product`
--

CREATE TABLE `product` (
  `id` bigint(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `category_id` bigint(32) NOT NULL,
  `description` varchar(255) DEFAULT NULL,
  `imgurl` varchar(255) NOT NULL,
  `last_update` datetime NOT NULL,
  `vendor_id` bigint(32) NOT NULL,
  `shipping_time` varchar(20) DEFAULT NULL,
  `shipping_rate` double DEFAULT NULL,
  `parent_path` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `product_img`
--

CREATE TABLE `product_img` (
  `id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `img_url` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `product_size_color_map`
--

CREATE TABLE `product_size_color_map` (
  `id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `size_id` bigint(20) DEFAULT NULL,
  `color_id` bigint(20) DEFAULT NULL,
  `price` double NOT NULL,
  `discount` double DEFAULT NULL,
  `quantity` bigint(32) NOT NULL,
  `product_length` double DEFAULT NULL,
  `product_width` double DEFAULT NULL,
  `product_height` double DEFAULT NULL,
  `product_weight` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `recent_view`
--

CREATE TABLE `recent_view` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `product_id` bigint(20) NOT NULL,
  `view_date` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `shipment_table`
--

CREATE TABLE `shipment_table` (
  `id` bigint(32) NOT NULL,
  `shipment_id` varchar(255) NOT NULL,
  `parcel_id` varchar(255) NOT NULL,
  `postage_label_id` varchar(255) NOT NULL,
  `rate_id` varchar(255) NOT NULL,
  `tracker_id` varchar(255) NOT NULL,
  `is_return` tinyint(1) NOT NULL,
  `customs_info_id` varchar(255) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `created_at` datetime NOT NULL,
  `status` varchar(255) NOT NULL,
  `pay_key` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `sizee`
--

CREATE TABLE `sizee` (
  `id` bigint(20) NOT NULL,
  `group_id` bigint(32) NOT NULL,
  `valu` varchar(20) NOT NULL,
  `unit` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `size_group`
--

CREATE TABLE `size_group` (
  `id` bigint(32) NOT NULL,
  `name` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `users`
--

CREATE TABLE `users` (
  `id` bigint(32) NOT NULL,
  `name` varchar(255) NOT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `email` varchar(255) NOT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `dob` date DEFAULT NULL,
  `reg_date` datetime DEFAULT NULL,
  `lastlogin_date` datetime DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `fbid` bigint(32) DEFAULT NULL,
  `profile_img` varchar(255) DEFAULT NULL,
  `fcm_token` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user_address`
--

CREATE TABLE `user_address` (
  `id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `status` bigint(20) NOT NULL,
  `street_one` varchar(255) NOT NULL,
  `street_two` varchar(255) DEFAULT NULL,
  `city` varchar(255) DEFAULT NULL,
  `state` varchar(255) DEFAULT NULL,
  `zip_code` varchar(255) DEFAULT NULL,
  `country` varchar(255) DEFAULT NULL,
  `residential` tinyint(1) DEFAULT NULL,
  `contact_name` varchar(255) DEFAULT NULL,
  `company_name` varchar(255) DEFAULT NULL,
  `phone` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `user_aouth`
--

CREATE TABLE `user_aouth` (
  `id` bigint(32) NOT NULL,
  `user_id` bigint(32) NOT NULL,
  `email` varchar(255) NOT NULL,
  `aouth_token` varchar(255) NOT NULL,
  `valid_till` datetime NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Table structure for table `vendor`
--

CREATE TABLE `vendor` (
  `id` bigint(32) NOT NULL,
  `vendor_name` varchar(255) NOT NULL,
  `street_one` varchar(255) NOT NULL,
  `street_two` varchar(255) DEFAULT NULL,
  `city` varchar(255) NOT NULL,
  `state` varchar(255) NOT NULL,
  `country` varchar(255) NOT NULL,
  `zip` varchar(255) NOT NULL,
  `phone` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `cart`
--
ALTER TABLE `cart`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `category`
--
ALTER TABLE `category`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `color`
--
ALTER TABLE `color`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `order_details`
--
ALTER TABLE `order_details`
  ADD PRIMARY KEY (`id`),
  ADD UNIQUE KEY `order_id` (`order_id`);

--
-- Indexes for table `otp_table`
--
ALTER TABLE `otp_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `payment_detail`
--
ALTER TABLE `payment_detail`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product`
--
ALTER TABLE `product`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product_img`
--
ALTER TABLE `product_img`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `product_size_color_map`
--
ALTER TABLE `product_size_color_map`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `recent_view`
--
ALTER TABLE `recent_view`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `shipment_table`
--
ALTER TABLE `shipment_table`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `sizee`
--
ALTER TABLE `sizee`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `size_group`
--
ALTER TABLE `size_group`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `users`
--
ALTER TABLE `users`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_address`
--
ALTER TABLE `user_address`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `user_aouth`
--
ALTER TABLE `user_aouth`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `vendor`
--
ALTER TABLE `vendor`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `cart`
--
ALTER TABLE `cart`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=7;
--
-- AUTO_INCREMENT for table `category`
--
ALTER TABLE `category`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=19;
--
-- AUTO_INCREMENT for table `color`
--
ALTER TABLE `color`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `order_details`
--
ALTER TABLE `order_details`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=59;
--
-- AUTO_INCREMENT for table `otp_table`
--
ALTER TABLE `otp_table`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT;
--
-- AUTO_INCREMENT for table `payment_detail`
--
ALTER TABLE `payment_detail`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;
--
-- AUTO_INCREMENT for table `product`
--
ALTER TABLE `product`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=30;
--
-- AUTO_INCREMENT for table `product_img`
--
ALTER TABLE `product_img`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=53;
--
-- AUTO_INCREMENT for table `product_size_color_map`
--
ALTER TABLE `product_size_color_map`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=40;
--
-- AUTO_INCREMENT for table `recent_view`
--
ALTER TABLE `recent_view`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=32;
--
-- AUTO_INCREMENT for table `shipment_table`
--
ALTER TABLE `shipment_table`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=15;
--
-- AUTO_INCREMENT for table `sizee`
--
ALTER TABLE `sizee`
  MODIFY `id` bigint(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;
--
-- AUTO_INCREMENT for table `size_group`
--
ALTER TABLE `size_group`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=3;
--
-- AUTO_INCREMENT for table `users`
--
ALTER TABLE `users`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=13;
--
-- AUTO_INCREMENT for table `user_address`
--
ALTER TABLE `user_address`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=10;
--
-- AUTO_INCREMENT for table `user_aouth`
--
ALTER TABLE `user_aouth`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=14;
--
-- AUTO_INCREMENT for table `vendor`
--
ALTER TABLE `vendor`
  MODIFY `id` bigint(32) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=2;