import React, { useState } from 'react';

function CheckboxField({ label, checked, onChange, name, dropdownContent }) {
    const [isOpen, setIsOpen] = useState(false);

    const toggleDropdown = (e) => {
        e.preventDefault(); // 기본 이벤트 중지
        setIsOpen(!isOpen);
    };

    return (
        <div className="checkbox-field">
            <input
                type="checkbox"
                checked={checked}
                onChange={onChange}
                name={name}
                style={{ marginRight: '10px' }}
            />
            <span onClick={toggleDropdown} style={{ textDecoration: 'underline', cursor: 'pointer' }}>
                {label}
            </span>
            {isOpen && (
                <div style={{ marginTop: '10px', border: '1px solid #ccc', padding: '10px' }}>
                    {dropdownContent}
                </div>
            )}
        </div>
    );
}

export default CheckboxField;
