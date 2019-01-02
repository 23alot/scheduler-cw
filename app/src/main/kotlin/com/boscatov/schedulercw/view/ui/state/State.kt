package com.boscatov.schedulercw.view.ui.state

sealed class State

class DefaultState : State()

class NewTaskState : State()