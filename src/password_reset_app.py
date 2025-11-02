import json
from pathlib import Path
from typing import Optional

import flet as ft

USERS_FILE = Path(__file__).parent / "data" / "users.json"
VERIFICATION_CODE = "9797"


def load_users():
    if not USERS_FILE.exists():
        return {"users": []}
    with USERS_FILE.open("r", encoding="utf-8") as file:
        return json.load(file)


def save_users(data: dict) -> None:
    USERS_FILE.parent.mkdir(parents=True, exist_ok=True)
    with USERS_FILE.open("w", encoding="utf-8") as file:
        json.dump(data, file, indent=2, ensure_ascii=False)


def reset_password(email: str, new_password: str, data: Optional[dict] = None) -> bool:
    data = data or load_users()
    for user in data.get("users", []):
        if user.get("email", "").lower() == email.lower():
            user["password"] = new_password
            save_users(data)
            return True
    return False


def main(page: ft.Page) -> None:
    page.title = "Zyra | Recuperación de contraseña"
    page.theme_mode = ft.ThemeMode.LIGHT
    page.horizontal_alignment = ft.CrossAxisAlignment.CENTER
    page.vertical_alignment = ft.MainAxisAlignment.CENTER
    page.window_width = 800
    page.window_height = 800
    page.window_resizable = False
    page.bgcolor = ft.colors.with_opacity(0.05, ft.colors.BLUE_GREY_100)

    status_text = ft.Text(value="", color=ft.colors.RED_400, size=12)

    email_field = ft.TextField(
        label="1. Ingrese su Email",
        hint_text="nombre@hospital.com",
        border_radius=30,
        bgcolor=ft.colors.WHITE,
        filled=True,
        prefix_icon=ft.icons.EMAIL_OUTLINED,
        width=360,
    )

    code_field = ft.TextField(
        label="2. Ingrese el Código de Verificación",
        hint_text="9-7-9-7",
        border_radius=30,
        bgcolor=ft.colors.WHITE,
        filled=True,
        prefix_icon=ft.icons.VERIFIED_OUTLINED,
        width=360,
    )

    password_field = ft.TextField(
        label="3. Ingrese Nueva Contraseña",
        border_radius=30,
        bgcolor=ft.colors.WHITE,
        filled=True,
        password=True,
        can_reveal_password=True,
        prefix_icon=ft.icons.LOCK_OUTLINED,
        width=360,
    )

    confirm_field = ft.TextField(
        label="4. Repite Nueva Contraseña",
        border_radius=30,
        bgcolor=ft.colors.WHITE,
        filled=True,
        password=True,
        can_reveal_password=True,
        prefix_icon=ft.icons.LOCK_OUTLINED,
        width=360,
    )

    def on_submit(e: ft.ControlEvent) -> None:
        status_text.color = ft.colors.RED_400
        status_text.value = ""

        email = email_field.value.strip()
        code = code_field.value.replace("-", "").replace(" ", "")
        new_password = password_field.value
        confirm_password = confirm_field.value
        users_data = load_users()
        users = users_data.get("users", [])
        matched_user = next(
            (user for user in users if user.get("email", "").lower() == email.lower()),
            None,
        )

        if not email:
            status_text.value = "Por favor, ingrese un email válido."
        elif not matched_user:
            status_text.value = "El email no se encuentra registrado."
        elif code != VERIFICATION_CODE:
            status_text.value = "Código de verificación incorrecto."
        elif not new_password or not confirm_password:
            status_text.value = "Debe ingresar y confirmar la nueva contraseña."
        elif new_password != confirm_password:
            status_text.value = "Las contraseñas no coinciden."
        else:
            if reset_password(email, new_password, users_data):
                status_text.color = ft.colors.GREEN_500
                status_text.value = "Contraseña actualizada correctamente."
                password_field.value = ""
                confirm_field.value = ""
                code_field.value = ""
            else:
                status_text.value = "No se pudo actualizar la contraseña."

        page.update()

    submit_button = ft.ElevatedButton(
        text="Cambiar Contraseña",
        bgcolor=ft.colors.BLUE_ACCENT,
        color=ft.colors.WHITE,
        width=220,
        height=48,
        style=ft.ButtonStyle(
            shape={"": ft.RoundedRectangleBorder(radius=25)},
            padding={"": 0},
        ),
        on_click=on_submit,
    )

    card = ft.Container(
        width=480,
        padding=ft.padding.only(top=40, bottom=60, left=40, right=40),
        bgcolor=ft.colors.WHITE,
        border_radius=30,
        shadow=ft.BoxShadow(
            spread_radius=2,
            blur_radius=25,
            color=ft.colors.with_opacity(0.15, ft.colors.BLUE_GREY_300),
            offset=ft.Offset(0, 8),
        ),
        content=ft.Column(
            horizontal_alignment=ft.CrossAxisAlignment.CENTER,
            controls=[
                ft.Column(
                    spacing=4,
                    horizontal_alignment=ft.CrossAxisAlignment.CENTER,
                    controls=[
                        ft.Image(
                            src="https://raw.githubusercontent.com/flet-dev/examples/main/assets/icons/icon-192.png",
                            width=64,
                            height=64,
                        ),
                        ft.Text(
                            "ZYRA",
                            weight=ft.FontWeight.BOLD,
                            color=ft.colors.BLUE_GREY_900,
                            size=22,
                        ),
                        ft.Text(
                            "hospital inteligente",
                            color=ft.colors.BLUE_GREY_400,
                            size=14,
                        ),
                    ],
                ),
                ft.Divider(height=30, color="transparent"),
                email_field,
                ft.Divider(height=10, color="transparent"),
                code_field,
                ft.Divider(height=10, color="transparent"),
                password_field,
                ft.Divider(height=10, color="transparent"),
                confirm_field,
                ft.Divider(height=20, color="transparent"),
                submit_button,
                ft.Divider(height=10, color="transparent"),
                status_text,
            ],
        ),
    )

    frame = ft.Container(
        width=800,
        height=800,
        bgcolor=ft.colors.WHITE,
        alignment=ft.alignment.center,
        content=card,
    )

    page.add(frame)


if __name__ == "__main__":
    ft.app(target=main)
