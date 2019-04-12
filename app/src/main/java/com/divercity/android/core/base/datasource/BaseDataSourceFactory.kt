package com.divercity.android.core.base.datasource

import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import com.divercity.android.core.base.usecase.Params
import com.divercity.android.core.base.usecase.UseCase

class BaseDataSourceFactory<T>(
    private val useCase: UseCase<List<T>, Params>,
    private val search: String?
) : DataSource.Factory<Long, T>() {

    val dataSource = MutableLiveData<BaseDataSource<T>>()

    override fun create(): DataSource<Long, T> {
        val data = BaseDataSource(useCase, search)
        dataSource.postValue(data)
        return data
    }
}
