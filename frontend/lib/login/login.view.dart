import 'package:flutter/foundation.dart';
import 'package:flutter/material.dart';
import 'package:hidroponia/shared/app.shared.dart';

class LoginPage extends StatefulWidget {
  const LoginPage({super.key});

  @override
  State<LoginPage> createState() => _LoginPageState();
}

class _LoginPageState extends State<LoginPage> {
  final GlobalKey<FormState> _formkey = GlobalKey<FormState>();
  Map<String, String> userCredentials = {
    "username": "",
    "password": "",
  };
  bool waitingReply = false;
  static const double inbetweenSpacing = 24;

  Future<void> _handleLogin() async {
    if (!_formkey.currentState!.validate()) return;
    setState(() {
      waitingReply = true;
    });
    try {
      final response = await apiClient.login(
          userCredentials['username']!, userCredentials['password']!);
      sharedUser['token'] = response['token'] ?? '';
      sharedUser['equipmentId'] = response['equipmentId'];
      sharedUser['username'] = response['username'];
      sharedUser['admin'] = response['admin'] ?? false;
      if (!mounted) return;
      Navigator.pushReplacementNamed(context, '/home');
    } catch (e) {
      if (kDebugMode) {
        print(e);
      }
      sharedSMS.currentState?.showSnackBar(
        SnackBar(content: Text('Falha no login: $e')),
      );
    } finally {
      if (mounted) {
        setState(() {
          waitingReply = false;
        });
      }
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: SingleChildScrollView(
        padding: const EdgeInsets.all(48),
        child: Wrap(
          alignment: WrapAlignment.center,
          runSpacing: inbetweenSpacing * 2,
          children: [
            Hero(
              tag: 'mainLogo',
              child: SizedBox.square(
                dimension: 200,
                child: Image.asset(
                  'lib/assets/logoCutTransparent.png',
                  fit: BoxFit.contain,
                  semanticLabel: "Logo Hidroponia",
                ),
              ),
            ),
            Form(
              key: _formkey,
              child: Column(
                children: [
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: "Nome de usuário",
                    ),
                    validator: (value) {
                      if (value != null && value.contains(" ")) {
                        return "Usuário inválido.";
                      } else if (value == "") {
                        return "Valor não pode ser vazio.";
                      }
                      return null;
                    },
                    onChanged: (value) {
                      userCredentials["username"] = value;
                    },
                  ),
                  TextFormField(
                    decoration: const InputDecoration(
                      labelText: "Senha",
                    ),
                    obscureText: true,
                    onChanged: (value) {
                      userCredentials["password"] = value;
                    },
                    validator: (value) {
                      if (value == "") {
                        return "Valor não pode ser vazio.";
                      }
                      return null;
                    },
                  ),
                ],
              ),
            ),
            Wrap(
              runSpacing: inbetweenSpacing / 2,
              children: [
                waitingReply == false
                    ? ElevatedButton(
                        style: const ButtonStyle(
                          minimumSize: MaterialStatePropertyAll(
                            Size(double.infinity, 38),
                          ),
                        ),
                        onPressed: _handleLogin,
                        child: !waitingReply
                            ? const Text("Login")
                            : const CircularProgressIndicator(),
                      )
                    : const Center(child: CircularProgressIndicator()),
                Row(
                  mainAxisAlignment: MainAxisAlignment.spaceBetween,
                  children: [
                    TextButton(
                      onPressed: () {
                        sharedSMS.currentState?.showSnackBar(
                          const SnackBar(
                            content: Text('Recuperação de senha não configurada.'),
                          ),
                        );
                      },
                      child: const Text("Esqueci a senha"),
                    ),
                    TextButton(
                      onPressed: () {
                        sharedSMS.currentState?.showSnackBar(
                          const SnackBar(
                              content: Text('Registro disponível via administrador.')),
                        );
                      },
                      child: const Text("Registrar"),
                    )
                  ],
                ),
              ],
            ),
            Container(
              alignment: Alignment.centerLeft,
              child: TextButton(
                onPressed: () {},
                child: const Text(
                  "Hidroponia Vetores por Vecteezy",
                  style: TextStyle(fontSize: 12),
                ),
              ),
            ),
          ],
        ),
      ),
    );
  }
}
