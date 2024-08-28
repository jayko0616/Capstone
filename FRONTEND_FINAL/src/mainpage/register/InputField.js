function InputField({ type, name, placeholder, value, onChange }) {
    return (
        <div className="input-field">
            <input
                type={type}
                name={name}
                placeholder={placeholder}
                value={value}
                onChange={onChange}
                required
            />
        </div>
    );
}

export default InputField;
