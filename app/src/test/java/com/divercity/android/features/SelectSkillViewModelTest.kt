package com.divercity.android.features

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.divercity.android.features.skill.base.SelectSkillViewModel
import com.divercity.android.repository.paginated.SkillPaginatedRepository
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito.*

/**
 * Created by lucas on 2019-06-24.
 */

@RunWith(JUnit4::class)
class SelectSkillViewModelTest {

    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    private val skillRepository = mock(SkillPaginatedRepository::class.java)
    private lateinit var viewModel: SelectSkillViewModel

    @Before
    fun init() {
        viewModel = SelectSkillViewModel(skillRepository)

    }

    @Test
    fun testSameSearchIsNotRepeated() {
        viewModel.fetchData(null, "foo")
        viewModel.fetchData(null, "foo")
        verify(skillRepository, times(1)).fetchData("foo")
    }

    @Test
    fun addition_isCorrect() {
        Assert.assertEquals(4, 2 + 2)
    }
}