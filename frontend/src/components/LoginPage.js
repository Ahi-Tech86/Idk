import * as React from 'react';

export default function Login() {
    return (
        <div className="Login-container">
            <form className="LoginForm">
                <label>Username:</label>
                <input type="text" />
                <br />
                <label>Password</label>
                <input type="text" />
                <br />
                <button type="submit">Login</button>
            </form>
        </div>
    );
}