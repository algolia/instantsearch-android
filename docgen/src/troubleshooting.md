---
title: Troubleshooting
layout: main.pug
name: troubleshooting
category: main
withHeadings: true
navWeight: 0
---


There are a few errors that you may encounter if your setup is not correct.
Here are the frequent errors and the appropriate solutions:

## No resource type specified
> Error:(42, 42) **No resource type specified** (at 'attribute' with value '@{"image"}’).

  Either you forgot to [enable the DataBinding Library](widgets.html#data-binding) or your `itemLayout` does not start with a `<layout>` root tag.

## NoClassDefFoundError
> **java.lang.NoClassDefFoundError: Failed resolution of: Landroid/databinding/DataBinderMapper;**
    at android.databinding.DataBindingUtil.<clinit>(DataBindingUtil.java:31)

  You forgot to [enable the DataBinding Library](widgets.html#data-binding).

## Only one layout element
> **Error:Only one layout element and one data element are allowed.** */path/to/your/app/build/intermediates/res/merged/debug/layout/hits_item.xml* has 2

Your `<layout>` should have only one child: put all current children in a single `<ViewGroup>`.

## Cannot find the setter for attribute 'algolia:attribute' with parameter type X on android.widget.Y

This is caused by your data-bound layout having an error in a binding expression.
Sadly the error message can be misleading, as the issue might come from any of the attributes.

Here's an example of an expression causing such an error:
```xml
        <TextView
            android:id="@+id/contactName"
            style="@style/AppTheme.View.ContactText"
            android:text="@string/placeholder_name"
            algolia:attribute='@{"name"}'
            algolia:highlightColor='@{"@color/colorAccent"}' />
```

This would trigger the error as `highlightColor` takes a `ColorInt` attribute while the expression contains a `String`.
In this case, changing it to `@{@color/colorAccent}` will resolve the issue (unless other expressions still have errors).
