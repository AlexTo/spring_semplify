import React, {Fragment, useEffect, useState} from "react";
import {
  Chip,
  Tooltip,
  Typography
} from "@material-ui/core";

import {withStyles} from '@material-ui/core/styles';


const HtmlTooltip = withStyles(theme => ({
  tooltip: {
    backgroundColor: '#f5f5f9',
    color: 'rgba(0, 0, 0, 0.87)',
    maxWidth: 400,
    fontSize: theme.typography.pxToRem(12),
    border: '1px solid #dadde9',
  },
}))(Tooltip);

function EntityChip({className, size, uri, label, prefLabel, color, onClick, onDelete}) {
  const [open, setOpen] = useState(false);

  const handleClose = () => {
    setOpen(false);
  };

  const handleOpen = () => {
    setOpen(true);
  };

  return (
    <HtmlTooltip
      title={
        <Fragment>
          <Typography color="inherit">{uri}</Typography>
          <div>{`surfaceForm: ${label}`}{' '}</div>
          <div>{`prefLabel: ${prefLabel}`}{' '}</div>

        </Fragment>
      }
      open={open} onClose={handleClose} onOpen={handleOpen} placement="top">
      <Chip
        className={className}
        size={size}
        label={label}
        color={color}
        onDelete={onDelete}
        onClick={onClick}/>
    </HtmlTooltip>
  )
}

export default EntityChip;
