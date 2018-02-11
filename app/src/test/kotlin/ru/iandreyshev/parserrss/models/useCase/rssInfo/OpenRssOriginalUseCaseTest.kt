package ru.iandreyshev.parserrss.models.useCase.rssInfo

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import ru.iandreyshev.parserrss.models.extention.uri

@RunWith(RobolectricTestRunner::class)
@Config(manifest = Config.NONE)
class OpenRssOriginalUseCaseTest {

    private lateinit var mListener: OpenRssOriginalUseCase.IListener

    @Before
    fun setup() {
        mListener = mock()
    }

    @Test
    fun callOpenOriginalWithValidUrl() {
        val url = "valid.url"

        OpenRssOriginalUseCase(url, mListener)
                .start()

        verify(mListener).openOriginal(url.uri)
    }

    @Test
    fun callOpenOriginalWithNullUrl() {
        OpenRssOriginalUseCase(null, mListener)
                .start()

        verify(mListener).openOriginal(null)
    }
}