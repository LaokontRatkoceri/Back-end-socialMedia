package com.example.di

import com.example.dao.follows.FollowDao
import com.example.dao.follows.FollowDaoImpl
import com.example.dao.user.UserDao
import com.example.dao.user.UserDaoImpl
import com.example.repository.auth.AuthRepository
import com.example.repository.auth.AuthRepositoryImpl
import com.example.repository.follows.FollowsRepository
import com.example.repository.follows.FollowsRepositoryImpl
import org.koin.dsl.module

val appModule = module {
    single<AuthRepository>{AuthRepositoryImpl(get())}
    single<UserDao>{ UserDaoImpl() }
    single<FollowDao>{FollowDaoImpl()}
    single<FollowsRepository>{ FollowsRepositoryImpl(get(), get()) }
}