package lol.kiyarash.weatherapp.data

import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.BindingAdapter


@BindingAdapter("android:src")
fun setImageSrc(view: ImageView, imageSrc: Int) {
    view.setImageResource(imageSrc)
}
@BindingAdapter("android:background")
fun setImageSrc(layout: ConstraintLayout, resourceId: Int) {
    layout.setBackgroundResource(resourceId)
}
